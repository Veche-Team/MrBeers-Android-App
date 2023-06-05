package com.example.beers_app.presentation.activity

import com.example.beers_app.domain.model.User

data class MainActivityState(
    val user: User = User(),
    val inCartQuantity: Int = 0
)
