package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.model.User
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.util.PasswordException

class DeleteUserUseCase(
    val appSettings: AppSettings,
    val userRepository: UserRepository
) {
    suspend operator fun invoke(password: String) {
        val user = appSettings.getCurrentUser()
        val userEntity = userRepository.findUserByNumber(user.phoneNumber)
        user.let {
            if (userEntity?.password != password) {
                throw PasswordException.OldPasswordException("Wrong password")
            }
            userRepository.deleteUser(user.phoneNumber)
            appSettings.setCurrentUser(User())
        }
    }
}