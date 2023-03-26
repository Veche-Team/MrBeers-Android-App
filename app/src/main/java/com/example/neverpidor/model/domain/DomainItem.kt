package com.example.neverpidor.model.domain

import androidx.annotation.DrawableRes
import com.example.neverpidor.data.Category

open class DomainItem(
     val category: Category,
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