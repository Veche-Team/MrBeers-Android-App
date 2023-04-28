package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.NumberAlreadyExistsException
import com.example.neverpidor.util.PasswordException

class RegisterUserUseCase(
    val repository: UserRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        name: String,
        password: String,
        repeatPassword: String
    ) {
        if (password != repeatPassword) {
            throw PasswordException.RepeatPasswordException("Passwords should match")
        }
        val users = repository.getAllUsers()
        users.forEach {
            if (it.phoneNumber == phoneNumber) {
                throw NumberAlreadyExistsException("User with this number already exists")
            }
        }
        val user = UserEntity(
            phoneNumber, name, password
        )
        repository.addUser(user)
    }
}