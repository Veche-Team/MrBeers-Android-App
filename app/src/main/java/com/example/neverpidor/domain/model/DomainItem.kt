package com.example.neverpidor.domain.model

import androidx.annotation.DrawableRes
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory

data class DomainItem(
    val category: MenuCategory = MenuCategory.SnackCategory,
    val UID: String = "",
    val alcPercentage: Double = 0.0,
    val description: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val type: String = "",
    val volume: Double = 0.0,
    @DrawableRes
     val image: Int = R.drawable.ic_baseline_remove_shopping_cart_24
) {
    fun toInCartItem(quantity: Int = 0): InCartItem {
        return InCartItem(
            UID = this.UID,
            price = this.price,
            quantity = quantity,
            title = this.name,
            image = this.image
        )
    }
}