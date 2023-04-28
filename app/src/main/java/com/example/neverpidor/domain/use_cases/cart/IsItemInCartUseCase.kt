package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.data.database.entities.UserMenuItemCart
import com.example.neverpidor.domain.repositories.CartRepository

class IsItemInCartUseCase(
    val cartRepository: CartRepository
) {
    suspend operator fun invoke(id: String, number: String): UserMenuItemCart? {
        return cartRepository.getItemInCart(number, id)

    }
}