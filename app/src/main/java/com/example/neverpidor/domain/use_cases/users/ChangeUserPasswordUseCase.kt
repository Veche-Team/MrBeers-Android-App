package com.example.neverpidor.domain.use_cases.users

import android.content.Context
import com.example.neverpidor.R
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.security.SecurityUtils

class ChangeUserPasswordUseCase(
    private val appSettings: AppSettings,
    private val userRepository: UserRepository,
    private val securityUtils: SecurityUtils,
    private val context: Context
) {
    suspend operator fun invoke(
        oldPassword: CharArray,
        newPassword: CharArray,
        repeatNewPassword: CharArray
    ) {
        if (oldPassword.contentEquals(newPassword)) {
            throw PasswordException.NewPasswordException(context.getString(R.string.new_password_exception))
        }

        if (!newPassword.contentEquals(repeatNewPassword)) {
            throw PasswordException.RepeatPasswordException(context.getString(R.string.passwords_should_match_exception))
        }
        val user = appSettings.getCurrentUser()
        val userEntity = userRepository.findUserByNumber(user.phoneNumber)!!
        val saltBytes = securityUtils.stringToBytes(userEntity.salt)
        val hashBytes = securityUtils.passwordToHash(oldPassword, saltBytes)
        val hashString = securityUtils.bytesToString(hashBytes)
        if (userEntity.hash != hashString) {
            throw PasswordException.OldPasswordException(context.getString(R.string.wrong_password_exception))
        }
        oldPassword.fill('*')
        repeatNewPassword.fill('*')
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