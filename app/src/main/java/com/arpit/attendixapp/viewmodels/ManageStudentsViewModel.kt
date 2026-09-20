package com.arpit.attendixapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.model.Student
import com.arpit.attendixapp.repository.StudentRepository
import com.arpit.attendixapp.util.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManageStudentsViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    val students: StateFlow<List<Student>> = studentRepository.getStudentsByAdmin(
        SessionManager.loggedInUserEmail ?: ""
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var showAddStudentDialog by mutableStateOf(false)

    var newStudentName by mutableStateOf("")
    var newStudentEnrollment by mutableStateOf("")
    var newStudentEmail by mutableStateOf("")
    var newStudentSemester by mutableStateOf("")

    fun onAddStudentClick() {
        showAddStudentDialog = true
    }

    fun onDismissAddStudentDialog() {
        showAddStudentDialog = false
        clearNewStudentFields()
    }

    fun onConfirmAddStudent() {
        val adminEmail = com.arpit.attendixapp.util.SessionManager.loggedInUserEmail ?: return
        if (newStudentName.isBlank() || newStudentEnrollment.isBlank()) return

        viewModelScope.launch {
            studentRepository.insertStudent(
                Student(
                    enrollmentNumber = newStudentEnrollment,
                    name = newStudentName,
                    email = newStudentEmail,
                    semester = newStudentSemester
                ),
                adminEmail
            )
            onDismissAddStudentDialog()
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.deleteStudentByEnrollment(student.enrollmentNumber)
        }
    }

    private fun clearNewStudentFields() {
        newStudentName = ""
        newStudentEnrollment = ""
        newStudentEmail = ""
        newStudentSemester = ""
    }
}
