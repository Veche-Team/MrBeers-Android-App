package com.example.neverpidor.data.settings

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.settings.AppSettings.Companion.NO_ITEM
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesAppSettings @Inject constructor(
    @ApplicationContext appContext: Context
) : AppSettings {

    private val sharedPreferences =
        appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun addListener(listener: OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun setCurrentCategory(category: MenuCategory) {
        sharedPreferences.edit()
            .putString(PREF_CURRENT_ITEM, category.toString())
            .apply()
    }

    override fun getCurrentCategory(): MenuCategory {
        val category = sharedPreferences.getString(PREF_CURRENT_ITEM, NO_ITEM) ?: ""
        return MenuCategory.toMenuCategory(category)
    }

    override fun getCurrentUser(): UserEntity {
        val user = sharedPreferences.getString(PREF_CURRENT_USER_NUMBER, null)
        user?.let {
            return GsonBuilder().create().fromJson(user, UserEntity::class.java)
        } ?: return UserEntity()
    }

    override fun setCurrentUser(user: UserEntity) {
        val gson = GsonBuilder()
        sharedPreferences.edit()
            .putString(PREF_CURRENT_USER_NUMBER, gson.create().toJson(user))
            .apply()
    }

    companion object {
        private const val PREF_CURRENT_ITEM = "currentItem"
         const val PREF_CURRENT_USER_NUMBER = "userNumber"
    }
}