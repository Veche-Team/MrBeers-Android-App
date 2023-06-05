package com.example.beers_app.domain.use_cases.cart

import com.example.beers_app.data.database.entities.UserMenuItemCart
import com.example.beers_app.domain.repositories.CartRepository

class IsItemInCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(id: String, number: String): UserMenuItemCart? {
        return cartRepository.getItemInCart(number, id)
    }
}