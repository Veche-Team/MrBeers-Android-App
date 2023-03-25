package com.example.neverpidor.ui.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neverpidor.data.repositories.BeersCategoryRepository
import com.example.neverpidor.data.MenuCategoryEntity
import com.example.neverpidor.model.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val appSettings: AppSettings
): ViewModel() {

    private val _menuCategoriesLiveData = MutableLiveData<List<MenuCategoryEntity>>()
    val menuCategoriesLiveData: LiveData<List<MenuCategoryEntity>> = _menuCategoriesLiveData

    private val repository = BeersCategoryRepository()

    fun getCategories() {
        _menuCategoriesLiveData.postValue(repository.getCategories())
    }

    fun setItem(item: Int) {
        appSettings.setCurrentItem(item)
    }
}