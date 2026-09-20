package com.arpit.attendixapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val email: String,
    val username: String,
    val password: String,
    val role: String // "Student" or "Admin"
)