package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.settings.AppSettings

class SetCurrentUserUseCase(
    private val appSettings: AppSettings
) {
    suspend operator fun invoke(user: UserEntity) {
        appSettings.setCurrentUser(user)
    }
}