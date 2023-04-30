package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.domain.repositories.CartRepository

class ClearUserCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(number: String) {
        cartRepository.clearUserCart(number)
    }
}