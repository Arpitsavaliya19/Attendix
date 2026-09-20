package com.arpit.attendixapp.data.repository

import com.arpit.attendixapp.data.local.dao.StaffDao
import com.arpit.attendixapp.data.local.entity.StaffEntity
import com.arpit.attendixapp.model.Staff
import com.arpit.attendixapp.repository.StaffRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultStaffRepository(
    private val staffDao: StaffDao
) : StaffRepository {
    override fun getStaffByAdmin(adminEmail: String): Flow<List<Staff>> {
        return staffDao.getStaffByAdmin(adminEmail).map { entities ->
            entities.map { entity ->
                Staff(
                    name = entity.name,
                    employeeId = entity.employeeId,
                    department = entity.department,
                    subject = entity.subject
                )
            }
        }
    }

    override suspend fun getStaffByAdminStatic(adminEmail: String): List<Staff> {
        return staffDao.getStaffByAdminStatic(adminEmail).map { entity ->
            Staff(
                name = entity.name,
                employeeId = entity.employeeId,
                department = entity.department,
                subject = entity.subject
            )
        }
    }

    override suspend fun insertStaff(staff: Staff, adminEmail: String) {
        staffDao.insertStaff(
            StaffEntity(
                employeeId = staff.employeeId,
                name = staff.name,
                department = staff.department,
                subject = staff.subject,
                addedByAdminEmail = adminEmail
            )
        )
    }

    override suspend fun deleteStaff(staff: Staff, adminEmail: String) {
        staffDao.deleteStaff(
            StaffEntity(
                employeeId = staff.employeeId,
                name = staff.name,
                department = staff.department,
                subject = staff.subject,
                addedByAdminEmail = adminEmail
            )
        )
    }

    override suspend fun deleteStaffById(empId: String) {
        staffDao.deleteStaffById(empId)
    }

    override suspend fun deleteStaffByAdmin(adminEmail: String) {
        staffDao.deleteStaffByAdmin(adminEmail)
    }
}
