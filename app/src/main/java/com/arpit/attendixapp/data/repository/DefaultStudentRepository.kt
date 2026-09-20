package com.arpit.attendixapp.data.repository

import com.arpit.attendixapp.data.local.dao.StudentDao
import com.arpit.attendixapp.data.local.entity.StudentEntity
import com.arpit.attendixapp.model.Student
import com.arpit.attendixapp.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultStudentRepository(
    private val studentDao: StudentDao
) : StudentRepository {
    override fun getStudentsByAdmin(adminEmail: String): Flow<List<Student>> {
        return studentDao.getStudentsByAdmin(adminEmail).map { entities ->
            entities.map { entity ->
                Student(
                    name = entity.name,
                    enrollmentNumber = entity.enrollmentNumber,
                    email = entity.email,
                    semester = entity.semester
                )
            }
        }
    }

    override suspend fun getStudentByEmail(email: String): Student? {
        val entity = studentDao.getStudentByEmail(email)
        return entity?.let {
            Student(
                name = it.name,
                enrollmentNumber = it.enrollmentNumber,
                email = it.email,
                semester = it.semester
            )
        }
    }

    override suspend fun getStudentsByAdminStatic(adminEmail: String): List<Student> {
        return studentDao.getStudentsByAdminStatic(adminEmail).map { entity ->
            Student(
                name = entity.name,
                enrollmentNumber = entity.enrollmentNumber,
                email = entity.email,
                semester = entity.semester
            )
        }
    }

    override suspend fun insertStudent(student: Student, adminEmail: String) {
        studentDao.insertStudent(
            StudentEntity(
                enrollmentNumber = student.enrollmentNumber,
                name = student.name,
                email = student.email,
                semester = student.semester,
                addedByAdminEmail = adminEmail
            )
        )
    }

    override suspend fun deleteStudent(student: Student, adminEmail: String) {
        studentDao.deleteStudent(
            StudentEntity(
                enrollmentNumber = student.enrollmentNumber,
                name = student.name,
                email = student.email,
                semester = student.semester,
                addedByAdminEmail = adminEmail
            )
        )
    }

    override suspend fun deleteStudentByEnrollment(enrollment: String) {
        studentDao.deleteStudentByEnrollment(enrollment)
    }

    override suspend fun deleteStudentsByAdmin(adminEmail: String) {
        studentDao.deleteStudentsByAdmin(adminEmail)
    }
}
