package com.example.neverpidor.data.settings

import androidx.datastore.preferences.core.Preferences
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AppSettings {

    fun addListener(): Flow<Preferences>

    suspend fun getCurrentCategory(): MenuCategory

   suspend fun setCurrentCategory(category: MenuCategory)

   suspend fun getCurrentUser() : User

   suspend fun setCurrentUser(user: User)
}