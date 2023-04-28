package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.settings.AppSettings

class GetUserUseCase(
    val appSettings: AppSettings
) {
    suspend operator fun invoke(): UserEntity {
        return appSettings.getCurrentUser()
    }
}