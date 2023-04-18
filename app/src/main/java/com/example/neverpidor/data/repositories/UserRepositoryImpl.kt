package com.example.neverpidor.data.repositories

import com.example.neverpidor.data.database.UserDao
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.database.entities.UserMenuItemJoin
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : com.example.neverpidor.domain.repositories.UserRepository {
    override suspend fun addUser(user: UserEntity) {
        userDao.addUser(user)
    }

    override suspend fun getAllUsers() = userDao.getAllUsers()

    override suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    override suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)

    override suspend fun addLike(userMenuItemJoin: UserMenuItemJoin) =
        userDao.addLike(userMenuItemJoin)

    override suspend fun removeLike(number: String, id: String) = userDao.removeLike(number, id)

    override suspend fun getUserLikes(number: String) = userDao.getUserLikes(number)

    override suspend fun findLike(number: String, id: String) = userDao.findLike(number, id)
}

// todo make interface for this
