package com.example.neverpidor.domain.model

import com.example.neverpidor.R

data class InCartItem(
    val UID: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val image: Int = R.drawable.ic_baseline_remove_shopping_cart_24
)
