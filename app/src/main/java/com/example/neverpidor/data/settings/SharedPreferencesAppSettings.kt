package com.example.neverpidor.data.settings

import android.content.Context
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.settings.AppSettings.Companion.NO_ITEM
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesAppSettings @Inject constructor(
    @ApplicationContext appContext: Context
) : AppSettings {

    private val sharedPreferences =
        appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun setCurrentItem(category: MenuCategory) {
        sharedPreferences.edit()
            .putString(PREF_CURRENT_ITEM, category.toString())
            .apply()
    }

    override fun getCurrentItem(): MenuCategory {
        val category = sharedPreferences.getString(PREF_CURRENT_ITEM, NO_ITEM) ?: ""
        return MenuCategory.toMenuCategory(category)
    }

    override fun getCurrentUserNumber(): Long {
        return sharedPreferences.getLong(PREF_CURRENT_USER_NUMBER, 0)

    }

    override fun setCurrentUser(phoneNumber: Long) {
        sharedPreferences.edit()
            .putLong(PREF_CURRENT_USER_NUMBER, phoneNumber)
            .apply()
    }

    companion object {
        private const val PREF_CURRENT_ITEM = "currentItem"
        private const val PREF_CURRENT_USER_NUMBER = "userNumber"
    }
}