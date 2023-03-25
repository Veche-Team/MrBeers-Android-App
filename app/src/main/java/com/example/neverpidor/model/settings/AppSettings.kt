package com.example.neverpidor.model.settings

import com.example.neverpidor.data.Category

interface AppSettings {

    fun getCurrentItem(): Category

    fun setCurrentItem(category: Category)

    companion object {
        const val NO_ITEM = ""
    }
}