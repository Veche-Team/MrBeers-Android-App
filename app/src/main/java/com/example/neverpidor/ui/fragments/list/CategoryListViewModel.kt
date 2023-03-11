package com.example.neverpidor.ui.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neverpidor.Repositories
import com.example.neverpidor.data.BeersCategoryRepository
import com.example.neverpidor.data.MenuCategoryEntity
import com.example.neverpidor.model.settings.AppSettings

class CategoryListViewModel(): ViewModel() {

    private val appSettings: AppSettings by lazy {
        Repositories.appSettings
    }

    private val _menuCategoriesLiveData = MutableLiveData<List<MenuCategoryEntity>>()
    val menuCategoriesLiveData: LiveData<List<MenuCategoryEntity>> = _menuCategoriesLiveData

    private val repository = BeersCategoryRepository()

    fun getCategories() {
        _menuCategoriesLiveData.value = repository.getCategories()
    }


    fun setItem(item: Int) {
        appSettings.setCurrentItem(item)
    }


}