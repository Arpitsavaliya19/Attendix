package com.arpit.attendixapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey
    val enrollmentNumber: String,
    val name: String,
    val email: String,
    val semester: String,
    val addedByAdminEmail: String = ""
)