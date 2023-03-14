package com.example.neverpidor.model.settings

import android.content.Context
import com.example.neverpidor.model.settings.AppSettings.Companion.NO_ITEM
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesAppSettings @Inject constructor(
    @ApplicationContext appContext: Context
) : AppSettings {

    private val sharedPreferences =
        appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun setCurrentItem(item: Int) {
        sharedPreferences.edit()
            .putInt(PREF_CURRENT_ITEM, item)
            .apply()
    }

    override fun getCurrentItem(): Int = sharedPreferences.getInt(PREF_CURRENT_ITEM, NO_ITEM)

    companion object {
        private const val PREF_CURRENT_ITEM = "currentItem"
    }
}