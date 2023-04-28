package com.example.neverpidor.domain.use_cases.users

import android.util.Log
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException

class DeleteUserUseCase(
    val appSettings: AppSettings,
    val userRepository: UserRepository
) {
    suspend operator fun invoke(password: String) {
        val user = appSettings.getCurrentUser()
        user.let {
            if (it.password != password) {
                throw PasswordException.OldPasswordException("Wrong password")
            }
            userRepository.deleteUser(user)
            appSettings.setCurrentUser(UserEntity())
        }
    }
}