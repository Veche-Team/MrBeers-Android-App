package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes

data class DomainSnack(
    override val itemType: String = "snack",
    override val UID: String,
    override val description: String,
    override val name: String,
    override val price: Double,
    override val type: String,
    @DrawableRes
    override val image: Int? = null,
    override val alcPercentage: Double = 0.0,
    override val volume: Double = 0.0,
    override val isFaved: Boolean = false,
    override val isInCart: Boolean = false
): DomainItem(itemType, UID, alcPercentage, description, name, price, type, volume, image)