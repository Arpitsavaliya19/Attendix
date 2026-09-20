package com.arpit.attendixapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.model.Staff
import com.arpit.attendixapp.repository.StaffRepository
import com.arpit.attendixapp.util.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManageStaffViewModel(private val staffRepository: StaffRepository) : ViewModel() {
    val staffList: StateFlow<List<Staff>> = staffRepository.getStaffByAdmin(
        SessionManager.loggedInUserEmail ?: ""
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var showAddStaffDialog by mutableStateOf(false)

    var newStaffName by mutableStateOf("")
    var newStaffEmployeeId by mutableStateOf("")
    var newStaffDepartment by mutableStateOf("")
    var newStaffSubject by mutableStateOf("")

    fun onAddStaffClick() {
        showAddStaffDialog = true
    }

    fun onDismissAddStaffDialog() {
        showAddStaffDialog = false
        clearNewStaffFields()
    }

    fun onConfirmAddStaff() {
        val adminEmail = com.arpit.attendixapp.util.SessionManager.loggedInUserEmail ?: return
        if (newStaffName.isBlank() || newStaffEmployeeId.isBlank()) return

        viewModelScope.launch {
            staffRepository.insertStaff(
                Staff(
                    employeeId = newStaffEmployeeId,
                    name = newStaffName,
                    department = newStaffDepartment,
                    subject = newStaffSubject
                ),
                adminEmail
            )
            onDismissAddStaffDialog()
        }
    }

    fun deleteStaff(staff: Staff) {
        viewModelScope.launch {
            staffRepository.deleteStaffById(staff.employeeId)
        }
    }

    private fun clearNewStaffFields() {
        newStaffName = ""
        newStaffEmployeeId = ""
        newStaffDepartment = ""
        newStaffSubject = ""
    }
}
