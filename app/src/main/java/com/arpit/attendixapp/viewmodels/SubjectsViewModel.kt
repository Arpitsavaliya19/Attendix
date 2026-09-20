package com.arpit.attendixapp.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.model.Subject
import com.arpit.attendixapp.repository.SubjectRepository
import com.arpit.attendixapp.util.NotificationHelper
import com.arpit.attendixapp.util.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SubjectsViewModel(
    application: Application,
    private val subjectRepository: SubjectRepository
) : AndroidViewModel(application) {
    
    private val _allAvailableSubjects = listOf(
        Subject(name = "Data Structures & Algorithms", colorHex = "#FF5722"),
        Subject(name = "Operating Systems", colorHex = "#2196F3"),
        Subject(name = "Database Management Systems", colorHex = "#4CAF50"),
        Subject(name = "Computer Networks", colorHex = "#9C27B0"),
        Subject(name = "Software Engineering", colorHex = "#FFC107"),
        Subject(name = "Cloud Computing", colorHex = "#00BCD4"),
        Subject(name = "Machine Learning", colorHex = "#E91E63"),
        Subject(name = "Artificial Intelligence", colorHex = "#795548")
    )

    val mySubjects: StateFlow<List<Subject>> = subjectRepository.getSubjectsByStudent(
        SessionManager.loggedInUserEmail ?: ""
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var availableSubjects by mutableStateOf(_allAvailableSubjects)
        private set

    init {
        viewModelScope.launch {
            mySubjects.collect { added ->
                availableSubjects = _allAvailableSubjects.filter { available ->
                    added.none { it.name == available.name }
                }
            }
        }
    }

    fun addSubject(subject: Subject) {
        val email = com.arpit.attendixapp.util.SessionManager.loggedInUserEmail ?: return
        viewModelScope.launch {
            subjectRepository.insertSubject(subject, email)
        }
    }

    fun removeSubject(subject: Subject) {
        viewModelScope.launch {
            subjectRepository.deleteSubjectByName(subject.name)
        }
    }

    fun markPresent(subject: Subject) {
        viewModelScope.launch {
            subjectRepository.markPresent(subject.id)
            checkAttendance(subject.id)
        }
    }

    fun markAbsent(subject: Subject) {
        viewModelScope.launch {
            subjectRepository.markAbsent(subject.id)
            checkAttendance(subject.id)
        }
    }

    private suspend fun checkAttendance(subjectId: Int) {
        val subject = subjectRepository.getSubjectById(subjectId) ?: return
        val criteria = com.arpit.attendixapp.util.SessionManager.attendanceCriteria
        if (subject.total >= 5) { // Minimum 5 classes to start alerts
            val percentage = (subject.attended.toFloat() / subject.total) * 100
            if (percentage < criteria) {
                NotificationHelper.showLowAttendanceNotification(
                    getApplication(),
                    subject.name,
                    percentage
                )
            }
        }
    }
}
