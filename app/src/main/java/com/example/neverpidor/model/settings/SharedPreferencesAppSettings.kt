package com.example.neverpidor.model.settings

import android.content.Context
import com.example.neverpidor.data.Category
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

    override fun setCurrentItem(category: Category) {
        sharedPreferences.edit()
            .putString(PREF_CURRENT_ITEM, category.toString())
            .apply()
    }

    override fun getCurrentItem(): Category {
        val category = sharedPreferences.getString(PREF_CURRENT_ITEM, NO_ITEM) ?: ""
        return Category.toCategory(category)
    }

    companion object {
        private const val PREF_CURRENT_ITEM = "currentItem"
    }
}