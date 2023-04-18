package com.example.neverpidor.presentation.fragments.itemlist

import com.example.neverpidor.domain.model.DomainItem

data class MenuItemListState(
    val menuItems: List<DomainItem> = emptyList(),
    val likedItems: List<String> = emptyList()
)
