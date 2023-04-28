package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.data.database.entities.UserMenuItemCart
import com.example.neverpidor.domain.repositories.CartRepository

class AddItemToCartUseCase(
    val cartRepository: CartRepository
) {
    suspend operator fun invoke(number: String, id: String) {
        cartRepository.addItemInCart(UserMenuItemCart(number, id))
    }
}