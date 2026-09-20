package com.arpit.attendixapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.model.AttendanceStatus
import com.arpit.attendixapp.model.Lecture
import com.arpit.attendixapp.model.Student
import com.arpit.attendixapp.repository.StudentRepository
import com.arpit.attendixapp.repository.SubjectRepository
import com.arpit.attendixapp.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MarkAttendanceViewModel(
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    val students: StateFlow<List<Student>> = studentRepository.getStudentsByAdmin(
        SessionManager.loggedInUserEmail ?: ""
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _studentSubjects = MutableStateFlow<List<Lecture>>(emptyList())
    val studentSubjects: StateFlow<List<Lecture>> = _studentSubjects

    var selectedStudent by mutableStateOf<Student?>(null)
    val attendanceStates = mutableStateMapOf<String, AttendanceStatus>()

    fun onStudentSelected(student: Student) {
        selectedStudent = student
        viewModelScope.launch {
            subjectRepository.getSubjectsByStudent(student.email).collect { subjects ->
                _studentSubjects.value = subjects.map { Lecture(it.name, it.id) }
            }
        }
    }

    fun onBackToStudentSelection() {
        selectedStudent = null
        attendanceStates.clear()
        _studentSubjects.value = emptyList()
    }

    fun onStatusChange(lecture: Lecture, status: AttendanceStatus) {
        attendanceStates[lecture.name] = status
        viewModelScope.launch {
            if (status == AttendanceStatus.PRESENT) {
                subjectRepository.markPresent(lecture.id)
            } else if (status == AttendanceStatus.ABSENT) {
                subjectRepository.markAbsent(lecture.id)
            }
        }
    }
}
