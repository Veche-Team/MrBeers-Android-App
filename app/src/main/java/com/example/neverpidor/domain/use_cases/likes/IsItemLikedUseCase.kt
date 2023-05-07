package com.example.neverpidor.domain.use_cases.likes

import com.example.neverpidor.data.database.entities.UserMenuItemLikes
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.LikesRepository

class IsItemLikedUseCase(
    private val appSettings: AppSettings,
    private val userRepository: LikesRepository
) {

    suspend operator fun invoke(id: String): UserMenuItemLikes? {
        val userNumber = appSettings.getCurrentUser().phoneNumber
        return userRepository.findLike(userNumber, id)
    }
}