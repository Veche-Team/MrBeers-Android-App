package com.example.neverpidor.domain.use_cases.users

import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.neverpidor.data.settings.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AddUserListenerUseCase(
    val appSettings: AppSettings
) {
     operator fun invoke(): Flow<String> {
        val userKey = stringPreferencesKey("user")

       return appSettings.addListener().map {
            it[userKey] ?: ""
        }.distinctUntilChanged()
    }
}