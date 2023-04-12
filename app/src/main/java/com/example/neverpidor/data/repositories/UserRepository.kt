package com.example.neverpidor.data.repositories

import com.example.neverpidor.data.database.UserDao
import com.example.neverpidor.data.database.entities.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    val userDao: UserDao
) {

    suspend fun addUser(user: UserEntity) {
        userDao.addUser(user)
    }
}