package com.arpit.attendixapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.model.Student
import com.arpit.attendixapp.repository.StudentRepository
import com.arpit.attendixapp.util.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ViewReportsViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    val students: StateFlow<List<Student>> = studentRepository.getStudentsByAdmin(
        SessionManager.loggedInUserEmail ?: ""
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
