package com.arpit.attendixapp.repository

import com.arpit.attendixapp.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    fun getSubjectsByStudent(studentEmail: String): Flow<List<Subject>>
    suspend fun getSubjectsByStudentStatic(studentEmail: String): List<Subject>
    suspend fun getSubjectById(id: Int): Subject?
    suspend fun insertSubject(subject: Subject, studentEmail: String)
    suspend fun deleteSubject(subject: Subject, studentEmail: String)
    suspend fun deleteSubjectByName(name: String)
    suspend fun deleteSubjectsByStudent(studentEmail: String)
    suspend fun markPresent(id: Int)
    suspend fun markAbsent(id: Int)
}
