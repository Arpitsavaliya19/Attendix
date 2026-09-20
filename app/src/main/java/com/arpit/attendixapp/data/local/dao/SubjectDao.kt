package com.arpit.attendixapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.arpit.attendixapp.data.local.entity.SubjectEntity

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects WHERE studentEmail = :studentEmail")
    fun getSubjectsByStudent(studentEmail: String): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE studentEmail = :studentEmail")
    suspend fun getSubjectsByStudentStatic(studentEmail: String): List<SubjectEntity>

    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun getSubjectById(id: Int): SubjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)

    @Query("DELETE FROM subjects WHERE name = :name")
    suspend fun deleteSubjectByName(name: String)

    @Query("DELETE FROM subjects WHERE studentEmail = :studentEmail")
    suspend fun deleteSubjectsByStudent(studentEmail: String)

    @Query("UPDATE subjects SET attendedClasses = attendedClasses + 1, totalClasses = totalClasses + 1 WHERE id = :id")
    suspend fun markPresent(id: Int)

    @Query("UPDATE subjects SET totalClasses = totalClasses + 1 WHERE id = :id")
    suspend fun markAbsent(id: Int)
}