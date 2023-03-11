package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes

data class DomainBeer(
    val UID: String,
    val alcPercentage: Double,
    val description: String,
    val name: String,
    val price: Double,
    val type: String,
    val volume: Double,
    @DrawableRes
    val image: Int? = null
)