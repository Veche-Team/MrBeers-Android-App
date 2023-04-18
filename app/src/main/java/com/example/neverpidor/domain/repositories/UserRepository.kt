package com.example.neverpidor.domain.repositories

import com.example.neverpidor.data.database.entities.UserAndMenuItems
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.database.entities.UserMenuItemJoin

interface UserRepository {

    suspend fun addUser(user: UserEntity)

    suspend fun getAllUsers(): List<UserEntity>

    suspend fun updateUser(user: UserEntity)

    suspend fun deleteUser(user: UserEntity)

    suspend fun addLike(userMenuItemJoin: UserMenuItemJoin)

    suspend fun removeLike(number: String, id: String)

    suspend fun getUserLikes(number: String): UserAndMenuItems

    suspend fun findLike(number: String, id: String): UserMenuItemJoin?
}