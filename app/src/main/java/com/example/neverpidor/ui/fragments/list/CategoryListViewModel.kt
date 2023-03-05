package com.example.neverpidor.ui.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neverpidor.data.BeersCategoryRepository
import com.example.neverpidor.data.MenuCategoryEntity

class CategoryListViewModel: ViewModel() {

    private val _menuCategoriesLiveData = MutableLiveData<List<MenuCategoryEntity>>()
    val menuCategoriesLiveData: LiveData<List<MenuCategoryEntity>> = _menuCategoriesLiveData

    private val repository = BeersCategoryRepository()

    fun getCategories() {
        _menuCategoriesLiveData.value = repository.getCategories()
    }


}