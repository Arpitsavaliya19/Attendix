package com.arpit.attendixapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.arpit.attendixapp.data.local.entity.StaffEntity

@Dao
interface StaffDao {
    @Query("SELECT * FROM staff WHERE addedByAdminEmail = :adminEmail")
    fun getStaffByAdmin(adminEmail: String): Flow<List<StaffEntity>>

    @Query("SELECT * FROM staff WHERE addedByAdminEmail = :adminEmail")
    suspend fun getStaffByAdminStatic(adminEmail: String): List<StaffEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: StaffEntity)

    @Delete
    suspend fun deleteStaff(staff: StaffEntity)

    @Query("DELETE FROM staff WHERE employeeId = :empId")
    suspend fun deleteStaffById(empId: String)

    @Query("DELETE FROM staff WHERE addedByAdminEmail = :adminEmail")
    suspend fun deleteStaffByAdmin(adminEmail: String)
}