package com.example.neverpidor.data.settings

import android.content.SharedPreferences
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.providers.MenuCategory

interface AppSettings {

    fun addListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    fun getCurrentCategory(): MenuCategory

    fun setCurrentCategory(category: MenuCategory)

    fun getCurrentUser() : UserEntity

    fun setCurrentUser(user: UserEntity)

    companion object {
        const val NO_ITEM = ""
    }
}