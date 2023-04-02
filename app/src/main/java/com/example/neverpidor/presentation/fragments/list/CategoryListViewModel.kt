package com.example.neverpidor.presentation.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val appSettings: AppSettings
): ViewModel() {

    private val _menuCategoriesLiveData = MutableLiveData<List<MenuCategory>>()
    val menuCategoriesLiveData: LiveData<List<MenuCategory>> = _menuCategoriesLiveData


    fun getCategories() {
        _menuCategoriesLiveData.postValue(listOf(MenuCategory.BeerCategory, MenuCategory.SnackCategory))
    }

    fun setItem(menuCategory: MenuCategory) {
        appSettings.setCurrentItem(menuCategory)
    }
}