package com.example.beers_app.domain.model

import com.example.beers_app.data.common.MenuCategory

data class DomainItem(
    val category: MenuCategory = MenuCategory.SnackCategory,
    val UID: String = "",
    val alcPercentage: Double = 0.0,
    val description: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val type: String = "",
    val salePercentage: Int = 0,
    val weight: Double = 0.0,
    val image: String? = null
) {
    val discountedPrice: Double
        get() = price - ((price * salePercentage) / 100)

    fun toInCartItem(quantity: Int = 0): InCartItem {
        return InCartItem(
            UID = this.UID,
            price = if (this.salePercentage == 0) this.price else this.discountedPrice,
            quantity = quantity,
            title = this.name,
            image = this.image
        )
    }
}