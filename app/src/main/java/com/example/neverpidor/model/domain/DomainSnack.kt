package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes

class DomainSnack(
    itemType: String = "snack",
    UID: String,
    description: String,
    name: String,
    price: Double,
    type: String,
    @DrawableRes
    image: Int? = null,
    alcPercentage: Double = 0.0,
    volume: Double = 0.0,
    isFaved: Boolean = false,
    isInCart: Boolean = false
) : DomainItem(itemType, UID, alcPercentage, description, name, price, type, volume, image)