package com.example.beers_app.domain.use_cases.users

import com.example.beers_app.data.settings.AppSettings
import com.example.beers_app.domain.repositories.UserRepository

class ChangeUserNameUseCase(
    private val appSettings: AppSettings,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(newName: String) {
        val user = appSettings.getCurrentUser()
        val newUser = user.copy(name = newName)
        userRepository.changeUserName(newUser.phoneNumber, newName)
        appSettings.setCurrentUser(newUser)
    }
}