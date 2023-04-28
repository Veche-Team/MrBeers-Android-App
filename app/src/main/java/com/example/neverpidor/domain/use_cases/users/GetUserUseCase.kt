package com.example.neverpidor.domain.use_cases.users

import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.model.User

class GetUserUseCase(
    val appSettings: AppSettings
) {
    suspend operator fun invoke(): User {
        return appSettings.getCurrentUser()
    }
}