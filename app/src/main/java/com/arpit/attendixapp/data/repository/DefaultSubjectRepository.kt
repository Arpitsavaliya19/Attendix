package com.arpit.attendixapp.data.repository

import com.arpit.attendixapp.data.local.dao.SubjectDao
import com.arpit.attendixapp.data.local.entity.SubjectEntity
import com.arpit.attendixapp.model.Subject
import com.arpit.attendixapp.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultSubjectRepository(
    private val subjectDao: SubjectDao
) : SubjectRepository {
    override fun getSubjectsByStudent(studentEmail: String): Flow<List<Subject>> {
        return subjectDao.getSubjectsByStudent(studentEmail).map { entities ->
            entities.map { entity ->
                Subject(
                    id = entity.id,
                    name = entity.name,
                    attended = entity.attendedClasses,
                    total = entity.totalClasses,
                    colorHex = entity.colorHex
                )
            }
        }
    }

    override suspend fun getSubjectsByStudentStatic(studentEmail: String): List<Subject> {
        return subjectDao.getSubjectsByStudentStatic(studentEmail).map { entity ->
            Subject(
                id = entity.id,
                name = entity.name,
                attended = entity.attendedClasses,
                total = entity.totalClasses,
                colorHex = entity.colorHex
            )
        }
    }

    override suspend fun getSubjectById(id: Int): Subject? {
        val entity = subjectDao.getSubjectById(id)
        return entity?.let {
            Subject(
                id = it.id,
                name = it.name,
                attended = it.attendedClasses,
                total = it.totalClasses,
                colorHex = it.colorHex
            )
        }
    }

    override suspend fun insertSubject(subject: Subject, studentEmail: String) {
        subjectDao.insertSubject(
            SubjectEntity(
                id = subject.id,
                name = subject.name,
                attendedClasses = subject.attended,
                totalClasses = subject.total,
                colorHex = subject.colorHex,
                studentEmail = studentEmail
            )
        )
    }

    override suspend fun deleteSubject(subject: Subject, studentEmail: String) {
        subjectDao.deleteSubject(
            SubjectEntity(
                id = subject.id,
                name = subject.name,
                attendedClasses = subject.attended,
                totalClasses = subject.total,
                colorHex = subject.colorHex,
                studentEmail = studentEmail
            )
        )
    }

    override suspend fun deleteSubjectByName(name: String) {
        subjectDao.deleteSubjectByName(name)
    }

    override suspend fun deleteSubjectsByStudent(studentEmail: String) {
        subjectDao.deleteSubjectsByStudent(studentEmail)
    }

    override suspend fun markPresent(id: Int) {
        subjectDao.markPresent(id)
    }

    override suspend fun markAbsent(id: Int) {
        subjectDao.markAbsent(id)
    }
}
