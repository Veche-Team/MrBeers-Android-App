package com.example.beers_app.domain.use_cases.cart

import com.example.beers_app.domain.model.InCartItem
import com.example.beers_app.data.database.entities.UserMenuItemCart
import com.example.beers_app.domain.repositories.CartRepository

class MinusItemInCartUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(number: String, inCartItem: InCartItem) {
        if (inCartItem.quantity > 1) {
            cartRepository.updateItemInCart(
                UserMenuItemCart(
                    number,
                    inCartItem.UID,
                    inCartItem.quantity - 1
                )
            )
        } else {
            cartRepository.removeItemFromCart(number, inCartItem.UID)
        }
    }
}