package com.example.neverpidor.domain.use_cases

import com.example.neverpidor.data.database.entities.UserMenuItemJoin
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository

class IsItemLikedUseCase(
    val appSettings: AppSettings,
    val userRepository: UserRepository
) {

    suspend operator fun invoke(id: String): UserMenuItemJoin? {
        val userNumber = appSettings.getCurrentUser().phoneNumber
        return userRepository.findLike(userNumber, id)
    }
}