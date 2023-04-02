package com.example.neverpidor.domain.model

import androidx.annotation.DrawableRes
import com.example.neverpidor.data.providers.MenuCategory

class DomainBeer(
    category: MenuCategory = MenuCategory.BeerCategory,
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