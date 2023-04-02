package com.example.neverpidor.domain.model

import androidx.annotation.DrawableRes
import com.example.neverpidor.data.providers.MenuCategory

class DomainSnack(
    category: MenuCategory = MenuCategory.SnackCategory,
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