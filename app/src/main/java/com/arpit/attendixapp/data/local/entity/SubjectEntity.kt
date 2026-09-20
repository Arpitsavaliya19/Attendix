package com.arpit.attendixapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val attendedClasses: Int = 0,
    val totalClasses: Int = 0,
    val colorHex: String = "#6200EE", // Default primary color
    val studentEmail: String = ""
)