package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.model.User
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.security.SecurityUtils

class DeleteUserUseCase(
    private val appSettings: AppSettings,
    private val userRepository: UserRepository,
    private val securityUtils: SecurityUtils
) {
    suspend operator fun invoke(password: CharArray) {
        val user = appSettings.getCurrentUser()
        val userEntity = userRepository.findUserByNumber(user.phoneNumber)!!
        val saltBytes = securityUtils.stringToBytes(userEntity.salt)
        val hashBytes = securityUtils.passwordToHash(password, saltBytes)
        password.fill('*')
        val hashString = securityUtils.bytesToString(hashBytes)

        if (userEntity.hash != hashString) {
            throw PasswordException.OldPasswordException("Wrong password")
        }
        userRepository.deleteUser(user.phoneNumber)
        appSettings.setCurrentUser(User())
    }
}