package com.example.beers_app.data.settings

import androidx.datastore.preferences.core.Preferences
import com.example.beers_app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AppSettings {

    fun addListener(): Flow<Preferences>

   suspend fun getCurrentUser() : User

   suspend fun setCurrentUser(user: User)
}