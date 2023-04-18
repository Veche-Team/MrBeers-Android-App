package com.example.neverpidor.domain.model

import androidx.annotation.DrawableRes
import com.example.neverpidor.data.providers.MenuCategory

open class DomainItem(
    val category: MenuCategory = MenuCategory.SnackCategory,
    val UID: String = "",
    val alcPercentage: Double = 0.0,
    val description: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val type: String = "",
    val volume: Double = 0.0,
    @DrawableRes
     val image: Int? = null,
    val isFaved: Boolean = false,
    val isInCart: Boolean = false
)