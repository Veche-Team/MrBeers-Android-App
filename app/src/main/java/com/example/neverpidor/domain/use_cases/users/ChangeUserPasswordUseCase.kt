package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.security.SecurityUtils

class ChangeUserPasswordUseCase(
    private val appSettings: AppSettings,
    private val userRepository: UserRepository,
    private val securityUtils: SecurityUtils
) {
    suspend operator fun invoke(
        oldPassword: CharArray,
        newPassword: CharArray,
        repeatNewPassword: CharArray
    ) {
        if (oldPassword.contentEquals(newPassword)) {
            throw PasswordException.NewPasswordException("New password is the same as old")
        }

        if (!newPassword.contentEquals(repeatNewPassword)) {
            throw PasswordException.RepeatPasswordException("Passwords should match")
        }
        repeatNewPassword.fill('*')
        val user = appSettings.getCurrentUser()
        val userEntity = userRepository.findUserByNumber(user.phoneNumber)!!
        val saltBytes = securityUtils.stringToBytes(userEntity.salt)
        val hashBytes = securityUtils.passwordToHash(oldPassword, saltBytes)
        oldPassword.fill('*')
        val hashString = securityUtils.bytesToString(hashBytes)
        if (userEntity.hash != hashString) {
            throw PasswordException.OldPasswordException("Wrong password")
        }
        val salt = securityUtils.generateSalt()
        val hash = securityUtils.passwordToHash(newPassword, salt)
        newPassword.fill('*')
        userRepository.changeUserPassword(
            user.phoneNumber,
            securityUtils.bytesToString(hash),
            securityUtils.bytesToString(salt)
        )
    }
}