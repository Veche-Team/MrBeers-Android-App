package com.example.beers_app.presentation.fragments.favourites.util

import com.example.beers_app.domain.model.DomainItem
import com.example.beers_app.domain.model.InCartItem
import com.example.beers_app.domain.model.User

data class FavouritesState(
    val likedItems: List<DomainItem> = emptyList(),
    val errorState: Boolean = false,
    val inCartItems: List<InCartItem> = emptyList(),
    val user: User = User()
)