package com.example.beers_app.data.settings

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.beers_app.domain.model.User
import com.example.beers_app.util.Constants.DATA_STORE_NAME
import com.example.beers_app.util.Constants.USER_PREFERENCES_KEY
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreAppSettings @Inject constructor(
    @ApplicationContext val appContext: Context
) : AppSettings {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        DATA_STORE_NAME
    )

    override fun addListener(): Flow<Preferences> {
        return appContext.dataStore.data
    }

    override suspend fun getCurrentUser(): User {
        val userKey = stringPreferencesKey(USER_PREFERENCES_KEY)
        val preferences = appContext.dataStore.data.first()
        preferences[userKey]?.let {
            val gson = GsonBuilder().registerTypeAdapter(User.Role::class.java, UserRoleTypeAdapter())
            return gson.create().fromJson(it, User::class.java)
        }
        return User(role = User.Role.NoUser)
    }

    override suspend fun setCurrentUser(user: User) {
        val userKey = stringPreferencesKey(USER_PREFERENCES_KEY)
        appContext.dataStore.edit {
            val gson = GsonBuilder().registerTypeAdapter(User.Role::class.java, UserRoleTypeAdapter())
            it[userKey] = gson.create().toJson(user)
            Log.e("settle", it[userKey] ?: "shaka")
        }
    }
}