package com.example.beers_app.domain.use_cases.cart

import com.example.beers_app.data.database.entities.UserMenuItemCart
import com.example.beers_app.domain.repositories.CartRepository

class AddItemToCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(number: String, id: String) {
        cartRepository.addItemInCart(UserMenuItemCart(number, id))
    }
}