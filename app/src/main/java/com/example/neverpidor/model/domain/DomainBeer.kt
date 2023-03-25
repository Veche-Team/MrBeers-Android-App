package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes

class DomainBeer(
    itemType: String = "beer",
    UID: String,
    alcPercentage: Double,
    description: String,
    name: String,
    price: Double,
    type: String,
    volume: Double,
    @DrawableRes
    image: Int? = null,
    isInCart: Boolean = false,
    isFaved: Boolean = false
) : DomainItem(itemType, UID, alcPercentage, description, name, price, type, volume, image)