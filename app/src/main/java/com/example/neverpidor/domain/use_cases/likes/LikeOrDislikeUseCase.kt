package com.example.neverpidor.domain.use_cases.likes

import com.example.neverpidor.data.database.entities.UserMenuItemLikes
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository

class LikeOrDislikeUseCase(
    private val appSettings: AppSettings,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(domainItemId: String, likedItems: List<String>): List<String> {
        val user = appSettings.getCurrentUser()
        if (user.name != "") {
            val likedItemsIds = userRepository.getUserLikes(user.phoneNumber).menuItems.map { it.UID }
            return if (likedItemsIds.contains(domainItemId)) {
                userRepository.removeLike(user.phoneNumber, domainItemId)
                likedItems.filterNot { it == domainItemId  }
            } else {
                userRepository.addLike(UserMenuItemLikes(user.phoneNumber, domainItemId))
                likedItems + domainItemId
            }
        }
        return emptyList()
    }
}