package com.example.beers_app.domain.use_cases.users

import android.content.Context
import com.example.beers.R
import com.example.beers_app.domain.model.User
import com.example.beers_app.domain.repositories.UserRepository
import com.example.beers_app.util.PasswordException
import com.example.beers_app.util.UserDoesntExistException
import com.example.beers_app.util.security.SecurityUtils

class FindUserByNumberUseCase(
    private val repository: UserRepository,
    private val securityUtils: SecurityUtils,
    private val context: Context
) {
    suspend operator fun invoke(number: String, password: CharArray): User {
        val user = repository.findUserByNumber(number)
            ?: throw UserDoesntExistException(context.getString(R.string.user_doesnt_exist_exception))
        val saltBytes = securityUtils.stringToBytes(user.salt)
        val hashBytes = securityUtils.passwordToHash(password, saltBytes)
        val hashString = securityUtils.bytesToString(hashBytes)
        password.fill('*')
        if (user.hash != hashString) {
            throw PasswordException.OldPasswordException(context.getString(R.string.wrong_password_exception))
        }
        return user.toUser()
    }
}