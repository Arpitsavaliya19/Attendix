package com.arpit.attendixapp.repository

import com.arpit.attendixapp.model.Lab
import kotlinx.coroutines.flow.Flow

interface LabRepository {
    fun getLabsByStudent(studentEmail: String): Flow<List<Lab>>
    suspend fun insertLab(lab: Lab, studentEmail: String)
    suspend fun deleteLab(lab: Lab, studentEmail: String)
    suspend fun deleteLabByName(name: String)
    suspend fun deleteLabsByStudent(studentEmail: String)
    suspend fun markPresent(id: Int)
    suspend fun markAbsent(id: Int)
}
