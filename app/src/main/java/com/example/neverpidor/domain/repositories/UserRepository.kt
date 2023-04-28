package com.example.neverpidor.domain.repositories

import com.example.neverpidor.data.database.entities.UserAndMenuItems
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.database.entities.UserMenuItemLikes
import com.example.neverpidor.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun addUser(user: UserEntity)

    suspend fun getAllUsers(): List<User>

    suspend fun updateUser(user: UserEntity)

    suspend fun deleteUser(number: String)

    suspend fun addLike(userMenuItemJoin: UserMenuItemLikes)

    suspend fun removeLike(number: String, id: String)

    suspend fun getUserLikes(number: String): UserAndMenuItems

    suspend fun findLike(number: String, id: String): UserMenuItemLikes?

    fun getItemLikesById(id: String): Flow<Int>

    suspend fun changeUserName(number: String, name: String)

    suspend fun changeUserPassword(number: String,password: String)

    suspend fun findUserByNumber(number: String): UserEntity?
}