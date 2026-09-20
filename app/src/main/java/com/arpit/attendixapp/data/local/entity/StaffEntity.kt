package com.arpit.attendixapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey
    val employeeId: String,
    val name: String,
    val department: String,
    val subject: String,
    val addedByAdminEmail: String = ""
)