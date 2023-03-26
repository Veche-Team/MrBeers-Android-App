package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes
import com.example.neverpidor.data.Category

class DomainSnack(
    category: Category = Category.Snack,
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
) : DomainItem(category, UID, alcPercentage, description, name, price, type, volume, image, isFaved, isInCart)