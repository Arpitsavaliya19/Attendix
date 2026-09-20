package com.arpit.attendixapp.model

data class User(
    val email: String,
    val username: String,
    val password: String,
    val role: String // "Student" or "Admin"
)
