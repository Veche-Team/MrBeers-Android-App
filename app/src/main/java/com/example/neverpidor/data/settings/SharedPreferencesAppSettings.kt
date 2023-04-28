package com.example.neverpidor.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.providers.MenuCategory
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesAppSettings @Inject constructor(
    @ApplicationContext val appContext: Context
) : AppSettings {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        "settings"
    )

    override fun addListener(): Flow<Preferences> {
        return appContext.dataStore.data
    }

    override suspend fun setCurrentCategory(category: MenuCategory) {
        val categoryKey = stringPreferencesKey("category")
        appContext.dataStore.edit {
            it[categoryKey] = category.toString()
        }
    }

    override suspend fun getCurrentCategory(): MenuCategory {
        val categoryKey = stringPreferencesKey("category")
        val preferences = appContext.dataStore.data.first()
        preferences[categoryKey]?.let {
            return MenuCategory.toMenuCategory(it)
        }
        return MenuCategory.BeerCategory
    }

    override suspend fun getCurrentUser(): UserEntity {
        val userKey = stringPreferencesKey("user")
        val preferences = appContext.dataStore.data.first()
        preferences[userKey]?.let {
            return GsonBuilder().create().fromJson(it, UserEntity::class.java)
        }
        return UserEntity()
    }

    override suspend fun setCurrentUser(user: UserEntity) {
        val userKey = stringPreferencesKey("user")
        appContext.dataStore.edit {
            it[userKey] = GsonBuilder().create().toJson(user)
        }
    }
}