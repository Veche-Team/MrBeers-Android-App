package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.UserDoesntExistException

class FindUserByNumberUseCase(
    val repository: UserRepository
) {
    suspend operator fun invoke(number: String, password: String): UserEntity {
        val users = repository.getAllUsers()
        val user = users.find {
            it.phoneNumber.contains(number)
        } ?: throw UserDoesntExistException("User with this number doesn't exist")
        if (user.password != password) {
            throw PasswordException.OldPasswordException("Wrong password")
        }
        return user
    }
}