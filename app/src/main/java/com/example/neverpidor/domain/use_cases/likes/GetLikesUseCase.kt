package com.example.neverpidor.domain.use_cases.likes

import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository

class GetLikesUseCase(
    private val appSettings: AppSettings,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<String> {
        val userNumber = appSettings.getCurrentUser().phoneNumber
        if (userNumber != "") {
            return userRepository.getUserLikes(userNumber).menuItems.map { it.UID }
        }
        return emptyList()
    }
}