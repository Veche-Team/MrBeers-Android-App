package com.example.neverpidor.data.repositories

import com.example.neverpidor.data.database.UserDao
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.database.entities.UserMenuItemLikes
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : com.example.neverpidor.domain.repositories.UserRepository {
    override suspend fun addUser(user: UserEntity) {
        userDao.addUser(user)
    }

    override suspend fun getAllUsers() = userDao.getAllUsers().map {
        it.toUser()
    }
// todo update delete
    override suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    override suspend fun deleteUser(number: String) = userDao.deleteUser(number)

    override suspend fun addLike(userMenuItemJoin: UserMenuItemLikes) =
        userDao.addLike(userMenuItemJoin)

    override suspend fun removeLike(number: String, id: String) = userDao.removeLike(number, id)

    override suspend fun getUserLikes(number: String) = userDao.getUserLikes(number)

    override suspend fun findLike(number: String, id: String) = userDao.findLike(number, id)

    override fun getItemLikesById(id: String) = userDao.getItemLikes(id)

    override suspend fun changeUserName(number: String, name: String) {
        userDao.changeUserName(number, name)
    }

    override suspend fun changeUserPassword(number: String, password: String) {
        userDao.changeUserPassword(number, password)
    }

    override suspend fun findUserByNumber(number: String): UserEntity? {
       return userDao.findUserByNumber(number)
    }
}
