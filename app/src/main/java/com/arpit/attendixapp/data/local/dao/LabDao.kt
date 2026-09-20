package com.arpit.attendixapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.arpit.attendixapp.data.local.entity.LabEntity

@Dao
interface LabDao {
    @Query("SELECT * FROM labs WHERE studentEmail = :studentEmail")
    fun getLabsByStudent(studentEmail: String): Flow<List<LabEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLab(lab: LabEntity)

    @Delete
    suspend fun deleteLab(lab: LabEntity)

    @Query("DELETE FROM labs WHERE name = :name")
    suspend fun deleteLabByName(name: String)

    @Query("DELETE FROM labs WHERE studentEmail = :studentEmail")
    suspend fun deleteLabsByStudent(studentEmail: String)

    @Query("UPDATE labs SET attendedClasses = attendedClasses + 1, totalClasses = totalClasses + 1 WHERE id = :id")
    suspend fun markPresent(id: Int)

    @Query("UPDATE labs SET totalClasses = totalClasses + 1 WHERE id = :id")
    suspend fun markAbsent(id: Int)
}