package com.example.beers_app.presentation.fragments.itemlist

import com.example.beers_app.domain.model.InCartItem
import com.example.beers_app.data.common.MenuCategory
import com.example.beers_app.domain.model.DomainItem
import com.example.beers_app.domain.model.User

data class MenuItemListState(
    val menuItems: List<DomainItem> = emptyList(),
    val likedItems: List<String> = emptyList(),
    val category: MenuCategory = MenuCategory.BeerCategory,
    val errorState: Boolean = false,
    val inCartItems: List<InCartItem> = emptyList() ,
    val user: User = User()
)
