package com.example.neverpidor.data.settings

import com.example.neverpidor.data.providers.MenuCategory

interface AppSettings {

    fun getCurrentItem(): MenuCategory

    fun setCurrentItem(category: MenuCategory)

    companion object {
        const val NO_ITEM = ""
    }
}