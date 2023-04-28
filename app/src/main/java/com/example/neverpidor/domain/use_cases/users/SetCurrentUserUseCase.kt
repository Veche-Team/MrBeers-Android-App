package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.model.User

class SetCurrentUserUseCase(
    private val appSettings: AppSettings
) {
    suspend operator fun invoke(user: User) {
        appSettings.setCurrentUser(user)
    }
}