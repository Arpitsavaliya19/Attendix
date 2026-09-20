package com.arpit.attendixapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.arpit.attendixapp.data.local.entity.StudentEntity

@Dao
interface StudentDao {
    @Query("SELECT * FROM students WHERE addedByAdminEmail = :adminEmail")
    fun getStudentsByAdmin(adminEmail: String): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE email = :email LIMIT 1")
    suspend fun getStudentByEmail(email: String): StudentEntity?

    @Query("SELECT * FROM students WHERE addedByAdminEmail = :adminEmail")
    suspend fun getStudentsByAdminStatic(adminEmail: String): List<StudentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    @Query("DELETE FROM students WHERE enrollmentNumber = :enrollment")
    suspend fun deleteStudentByEnrollment(enrollment: String)

    @Query("DELETE FROM students WHERE addedByAdminEmail = :adminEmail")
    suspend fun deleteStudentsByAdmin(adminEmail: String)
}