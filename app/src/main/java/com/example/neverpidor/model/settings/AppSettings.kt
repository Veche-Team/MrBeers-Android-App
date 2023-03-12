package com.example.neverpidor.model.settings

interface AppSettings {

    fun getCurrentItem(): Int

    fun setCurrentItem(item: Int)

    companion object {
        const val NO_ITEM = -1
    }
}