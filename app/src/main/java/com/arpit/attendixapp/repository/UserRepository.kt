package com.arpit.attendixapp.repository

import com.arpit.attendixapp.model.User

interface UserRepository {
    suspend fun getUserByEmail(email: String): User?
    suspend fun insertUser(user: User)
    suspend fun getUserCount(): Int
    suspend fun updatePasswordByEmail(email: String, newPassword: String): Boolean
}
