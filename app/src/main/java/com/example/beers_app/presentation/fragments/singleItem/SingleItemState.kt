package com.example.beers_app.presentation.fragments.singleItem

import com.example.beers_app.domain.model.InCartItem
import com.example.beers_app.domain.model.DomainItem
import com.example.beers_app.domain.model.User

data class SingleItemState(
    val mainItem: DomainItem = DomainItem(),
    val likedItems: List<String> = emptyList(),
    val itemsSet: Set<DomainItem> = emptySet(),
    val isMainItemLiked: Boolean = false,
    val isMainItemInCart: Boolean = false,
    val inCartItem: InCartItem = InCartItem(quantity = 0),
    val user: User = User(),
    val inCartItemsSet: List<InCartItem> = emptyList()
)