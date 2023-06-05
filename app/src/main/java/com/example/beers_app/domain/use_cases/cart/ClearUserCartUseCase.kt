package com.example.beers_app.domain.use_cases.cart

import com.example.beers_app.domain.repositories.CartRepository

class ClearUserCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(number: String) {
        cartRepository.clearUserCart(number)
    }
}