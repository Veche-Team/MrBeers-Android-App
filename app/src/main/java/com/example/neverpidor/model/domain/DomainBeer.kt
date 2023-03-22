package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes

data class DomainBeer(
    override val itemType: String = "beer",
    override val UID: String,
    override val alcPercentage: Double,
    override val description: String,
    override val name: String,
    override val price: Double,
    override val type: String,
    override val volume: Double,
    @DrawableRes
    override val image: Int? = null,
    override val isInCart: Boolean = false,
    override val isFaved: Boolean = false
): DomainItem(itemType, UID, alcPercentage, description, name, price, type, volume, image, )