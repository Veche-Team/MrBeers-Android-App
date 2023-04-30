package com.example.neverpidor.presentation.fragments.itemlist

import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.model.User

data class MenuItemListState(
    val menuItems: List<DomainItem> = emptyList(),
    val likedItems: List<String> = emptyList(),
    val category: MenuCategory = MenuCategory.BeerCategory,
    val errorState: Boolean = false,
    val inCartItems: List<InCartItem> = emptyList() ,
    val user: User = User()
)
