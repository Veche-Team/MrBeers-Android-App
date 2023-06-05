package com.example.beers_app.domain.use_cases.users

import com.example.beers_app.data.settings.AppSettings
import com.example.beers_app.domain.model.User

class GetUserUseCase(
    private val appSettings: AppSettings
) {
    suspend operator fun invoke(): User {
        return appSettings.getCurrentUser()
    }
}