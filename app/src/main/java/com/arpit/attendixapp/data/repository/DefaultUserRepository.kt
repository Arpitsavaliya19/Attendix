package com.arpit.attendixapp.data.repository

import com.arpit.attendixapp.data.local.dao.UserDao
import com.arpit.attendixapp.data.local.entity.UserEntity
import com.arpit.attendixapp.model.User
import com.arpit.attendixapp.repository.UserRepository

class DefaultUserRepository(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun getUserByEmail(email: String): User? {
        val entity = userDao.getUserByEmail(email)
        return entity?.let {
            User(
                email = it.email,
                username = it.username,
                password = it.password,
                role = it.role
            )
        }
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(
            UserEntity(
                email = user.email,
                username = user.username,
                password = user.password,
                role = user.role
            )
        )
    }

    override suspend fun getUserCount(): Int {
        return userDao.getUserCount()
    }

    override suspend fun updatePasswordByEmail(email: String, newPassword: String): Boolean {
        return userDao.updatePasswordByEmail(email, newPassword) > 0
    }
}
