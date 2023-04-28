package com.example.neverpidor.domain.use_cases.menu_items

import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.settings.AppSettings

class SetCurrentCategoryUseCase(
    val appSettings: AppSettings
) {
    suspend operator fun invoke(menuCategory: MenuCategory) {
        appSettings.setCurrentCategory(menuCategory)
    }
}