package com.arpit.attendixapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "labs")
data class LabEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val attendedClasses: Int = 0,
    val totalClasses: Int = 0,
    val colorHex: String = "#03A9F4",
    val studentEmail: String = ""
)