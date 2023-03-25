package com.example.neverpidor.data.repositories

import com.example.neverpidor.data.MenuCategoryEntity

class BeersCategoryRepository {

    fun getCategories() = MenuCategoryEntity.getCategories()
}