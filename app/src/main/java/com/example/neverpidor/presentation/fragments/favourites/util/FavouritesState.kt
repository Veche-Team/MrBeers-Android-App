package com.example.neverpidor.presentation.fragments.favourites.util

import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.domain.model.User

data class FavouritesState(
    val likedItems: List<DomainItem> = emptyList(),
    val errorState: Boolean = false,
    val inCartItems: List<InCartItem> = emptyList(),
    val user: User = User()
)