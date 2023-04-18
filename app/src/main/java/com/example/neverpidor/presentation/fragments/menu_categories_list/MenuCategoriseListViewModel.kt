package com.example.neverpidor.presentation.fragments.menu_categories_list

import androidx.lifecycle.ViewModel
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuCategoriseListViewModel @Inject constructor(
    private val appSettings: AppSettings
) : ViewModel() {

    fun setItem(menuCategory: MenuCategory) {
        appSettings.setCurrentCategory(menuCategory)
    }
}