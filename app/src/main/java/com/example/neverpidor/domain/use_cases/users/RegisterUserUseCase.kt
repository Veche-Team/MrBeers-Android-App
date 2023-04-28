package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.presentation.fragments.register.util.RegisterInputFields
import com.example.neverpidor.util.NumberAlreadyExistsException
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.security.SecurityUtils

class RegisterUserUseCase(
    val repository: UserRepository,
    private val securityUtils: SecurityUtils
) {
    suspend operator fun invoke(
        registerInputFields: RegisterInputFields
    ) {
        if (registerInputFields.password != registerInputFields.repeatPassword) {
            throw PasswordException.RepeatPasswordException("Passwords should match")
        }
        val users = repository.getAllUsers()
        users.forEach {
            if (it.phoneNumber == registerInputFields.number) {
                throw NumberAlreadyExistsException("User with this number already exists")
            }
        }
        val salt = securityUtils.generateSalt()
        val hash = securityUtils.passwordToHash(registerInputFields.password.toCharArray(), salt)
        val user = UserEntity(
            phoneNumber = registerInputFields.number,
            name = registerInputFields.name,
         /*   hash = securityUtils.bytesToString(hash),
            salt = securityUtils.bytesToString(salt)*/
        password = registerInputFields.password
        )
        repository.addUser(user)
    }
}