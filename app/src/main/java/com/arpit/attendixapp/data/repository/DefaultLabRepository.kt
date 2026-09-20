package com.arpit.attendixapp.data.repository

import com.arpit.attendixapp.data.local.dao.LabDao
import com.arpit.attendixapp.data.local.entity.LabEntity
import com.arpit.attendixapp.model.Lab
import com.arpit.attendixapp.repository.LabRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultLabRepository(
    private val labDao: LabDao
) : LabRepository {
    override fun getLabsByStudent(studentEmail: String): Flow<List<Lab>> {
        return labDao.getLabsByStudent(studentEmail).map { entities ->
            entities.map { entity ->
                Lab(
                    id = entity.id,
                    name = entity.name,
                    attended = entity.attendedClasses,
                    total = entity.totalClasses,
                    colorHex = entity.colorHex
                )
            }
        }
    }

    override suspend fun insertLab(lab: Lab, studentEmail: String) {
        labDao.insertLab(
            LabEntity(
                id = lab.id,
                name = lab.name,
                attendedClasses = lab.attended,
                totalClasses = lab.total,
                colorHex = lab.colorHex,
                studentEmail = studentEmail
            )
        )
    }

    override suspend fun deleteLab(lab: Lab, studentEmail: String) {
        labDao.deleteLab(
            LabEntity(
                id = lab.id,
                name = lab.name,
                attendedClasses = lab.attended,
                totalClasses = lab.total,
                colorHex = lab.colorHex,
                studentEmail = studentEmail
            )
        )
    }

    override suspend fun deleteLabByName(name: String) {
        labDao.deleteLabByName(name)
    }

    override suspend fun deleteLabsByStudent(studentEmail: String) {
        labDao.deleteLabsByStudent(studentEmail)
    }

    override suspend fun markPresent(id: Int) {
        labDao.markPresent(id)
    }

    override suspend fun markAbsent(id: Int) {
        labDao.markAbsent(id)
    }
}
