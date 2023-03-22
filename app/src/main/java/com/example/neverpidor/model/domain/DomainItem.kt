package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes

open class DomainItem(
    open val itemType: String,
    open val UID: String = "",
    open val alcPercentage: Double = 0.0,
    open val description: String = "",
    open val name: String = "",
    open val price: Double = 0.0,
    open val type: String = "",
    open val volume: Double = 0.0,
    @DrawableRes
    open val image: Int? = null,
    open val isFaved: Boolean = false,
    open val isInCart: Boolean = false
)