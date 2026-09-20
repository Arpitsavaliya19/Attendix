package com.arpit.attendixapp.repository

import com.arpit.attendixapp.model.Staff
import kotlinx.coroutines.flow.Flow

interface StaffRepository {
    fun getStaffByAdmin(adminEmail: String): Flow<List<Staff>>
    suspend fun getStaffByAdminStatic(adminEmail: String): List<Staff>
    suspend fun insertStaff(staff: Staff, adminEmail: String)
    suspend fun deleteStaff(staff: Staff, adminEmail: String)
    suspend fun deleteStaffById(empId: String)
    suspend fun deleteStaffByAdmin(adminEmail: String)
}
