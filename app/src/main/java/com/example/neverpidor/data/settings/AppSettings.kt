package com.example.neverpidor.data.settings

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.providers.MenuCategory

interface AppSettings {

    fun getCurrentItem(): MenuCategory

    fun setCurrentItem(category: MenuCategory)

    fun getCurrentUserNumber() : Long?

    fun setCurrentUser(phoneNumber: Long)

    companion object {
        const val NO_ITEM = ""
    }
}