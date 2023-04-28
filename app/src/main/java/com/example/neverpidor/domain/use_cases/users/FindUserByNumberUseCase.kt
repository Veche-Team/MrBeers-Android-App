package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.domain.model.User
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.UserDoesntExistException

class FindUserByNumberUseCase(
    val repository: UserRepository
) {
    suspend operator fun invoke(number: String, password: String): User {
        val user = repository.findUserByNumber(number) ?: throw UserDoesntExistException("User with this number doesn't exist")
        if (user.password != password) {
            throw PasswordException.OldPasswordException("Wrong password")
        }
        return user.toUser()
    }
}