package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes
import com.example.neverpidor.data.Category

class DomainBeer(
    category: Category = Category.Beer,
    UID: String,
    alcPercentage: Double,
    description: String,
    name: String,
    price: Double,
    type: String,
    volume: Double,
    @DrawableRes
    image: Int? = null,
    isFaved: Boolean = false,
    isInCart: Boolean = false
) : DomainItem(category, UID, alcPercentage, description, name, price, type, volume, image, isFaved, isInCart)