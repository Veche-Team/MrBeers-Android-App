package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException

class ChangeUserPasswordUseCase(
    val appSettings: AppSettings,
    val userRepository: UserRepository
) {
    suspend operator fun invoke(
        oldPassword: String,
        newPassword: String,
        repeatNewPassword: String
    ) {
        if (oldPassword == newPassword) {
            throw PasswordException.NewPasswordException("New password is the same as old")
        }
        if (newPassword != repeatNewPassword) {
            throw PasswordException.RepeatPasswordException("Passwords should match")
        }

        val user = appSettings.getCurrentUser()
        user.let {
            if (it.password != oldPassword) {
                throw PasswordException.OldPasswordException("Wrong password")
            }
            val newUser = it.copy(password = newPassword)
            userRepository.updateUser(newUser)
            appSettings.setCurrentUser(newUser)
        }
    }
}