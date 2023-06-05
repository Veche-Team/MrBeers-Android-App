package com.example.beers_app.domain.use_cases.cart

import com.example.beers_app.data.database.entities.UserMenuItemCart
import com.example.beers_app.domain.repositories.CartRepository

class PlusItemInCartUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(number: String, itemId: String, itemQuantity: Int) {
        cartRepository.updateItemInCart(
            UserMenuItemCart(
                number,
                itemId,
                itemQuantity + 1
            )
        )
    }
}