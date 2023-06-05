package com.example.beers_app.domain.repositories

import com.example.beers_app.data.database.entities.UserEntity

interface UserRepository {

    suspend fun addUser(user: UserEntity)

    suspend fun updateUser(user: UserEntity)

    suspend fun deleteUser(number: String)

    suspend fun changeUserName(number: String, name: String)

    suspend fun changeUserPassword(number: String, hash: String, salt: String)

    suspend fun findUserByNumber(number: String): UserEntity?
}