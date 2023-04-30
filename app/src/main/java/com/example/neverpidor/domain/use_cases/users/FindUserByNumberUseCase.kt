package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.domain.model.User
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.UserDoesntExistException
import com.example.neverpidor.util.security.SecurityUtils

class FindUserByNumberUseCase(
    private val repository: UserRepository,
    private val securityUtils: SecurityUtils
) {
    suspend operator fun invoke(number: String, password: CharArray): User {
        val user = repository.findUserByNumber(number)
            ?: throw UserDoesntExistException("User with this number doesn't exist")
        val saltBytes = securityUtils.stringToBytes(user.salt)
        val hashBytes = securityUtils.passwordToHash(password, saltBytes)
        val hashString = securityUtils.bytesToString(hashBytes)
        password.fill('*')
        if (user.hash != hashString) {
            throw PasswordException.OldPasswordException("Wrong password")
        }
        return user.toUser()
    }
}