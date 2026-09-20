package com.arpit.attendixapp.repository

import com.arpit.attendixapp.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun getStudentsByAdmin(adminEmail: String): Flow<List<Student>>
    suspend fun getStudentByEmail(email: String): Student?
    suspend fun getStudentsByAdminStatic(adminEmail: String): List<Student>
    suspend fun insertStudent(student: Student, adminEmail: String)
    suspend fun deleteStudent(student: Student, adminEmail: String)
    suspend fun deleteStudentByEnrollment(enrollment: String)
    suspend fun deleteStudentsByAdmin(adminEmail: String)
}
