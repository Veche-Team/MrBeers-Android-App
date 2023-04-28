package com.example.neverpidor.presentation.fragments.singleItem

import com.example.neverpidor.data.cart.InCartItem
import com.example.neverpidor.domain.model.DomainItem

data class SingleItemState(
    val mainItem: DomainItem = DomainItem(),
    val likedItems: List<String> = emptyList(),
    val itemsSet: Set<DomainItem> = emptySet(),
    val isMainItemLiked: Boolean = false,
    val isMainItemInCart: Boolean = false,
    val inCartItem: InCartItem = InCartItem(quantity = 0)
)