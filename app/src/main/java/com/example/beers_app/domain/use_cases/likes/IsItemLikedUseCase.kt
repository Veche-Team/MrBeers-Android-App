package com.example.beers_app.domain.use_cases.likes

import com.example.beers_app.data.database.entities.UserMenuItemLikes
import com.example.beers_app.data.settings.AppSettings
import com.example.beers_app.domain.repositories.LikesRepository

class IsItemLikedUseCase(
    private val appSettings: AppSettings,
    private val userRepository: LikesRepository
) {

    suspend operator fun invoke(id: String): UserMenuItemLikes? {
        val userNumber = appSettings.getCurrentUser().phoneNumber
        return userRepository.findLike(userNumber, id)
    }
}