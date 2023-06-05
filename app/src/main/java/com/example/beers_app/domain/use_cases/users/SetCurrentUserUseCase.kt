package com.example.beers_app.domain.use_cases.users

import com.example.beers_app.data.settings.AppSettings
import com.example.beers_app.domain.model.User

class SetCurrentUserUseCase(
    private val appSettings: AppSettings
) {
    suspend operator fun invoke(user: User) {
        appSettings.setCurrentUser(user)
    }
}