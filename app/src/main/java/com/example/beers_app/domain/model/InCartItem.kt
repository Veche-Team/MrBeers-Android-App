package com.example.beers_app.domain.model

data class InCartItem(
    val UID: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val image: String? = null
)
