package com.arpit.attendixapp.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.AttendixApplication
import com.arpit.attendixapp.util.SessionManager
import com.arpit.attendixapp.repository.LabRepository
import com.arpit.attendixapp.repository.StaffRepository
import com.arpit.attendixapp.repository.StudentRepository
import com.arpit.attendixapp.repository.SubjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    application: Application,
    private val subjectRepository: SubjectRepository,
    private val labRepository: LabRepository,
    private val studentRepository: StudentRepository,
    private val staffRepository: StaffRepository
) : AndroidViewModel(application) {
    private val database = (application as AttendixApplication).database
    
    var showCriteriaDialog by mutableStateOf(false)
    var newCriteria by mutableStateOf(SessionManager.attendanceCriteria.toString())

    fun updateCriteria() {
        val criteriaValue = newCriteria.toFloatOrNull() ?: 75f
        SessionManager.updateCriteria(criteriaValue)
        showCriteriaDialog = false
    }

    fun resetSubjects() {
        val email = SessionManager.loggedInUserEmail ?: return
        viewModelScope.launch(Dispatchers.IO) {
            subjectRepository.deleteSubjectsByStudent(email)
            labRepository.deleteLabsByStudent(email)
        }
    }

    fun resetData() {
        val email = SessionManager.loggedInUserEmail ?: return
        viewModelScope.launch(Dispatchers.IO) {
            // Delete all data related to the current user
            subjectRepository.deleteSubjectsByStudent(email)
            labRepository.deleteLabsByStudent(email)
            studentRepository.deleteStudentsByAdmin(email)
            staffRepository.deleteStaffByAdmin(email)
        }
    }

    fun resetApp() {
        viewModelScope.launch(Dispatchers.IO) {
            database.clearAllTables()
            withContext(Dispatchers.Main) {
                SessionManager.logout()
            }
        }
    }
}
