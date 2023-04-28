package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.data.database.entities.UserMenuItemCart
import com.example.neverpidor.domain.repositories.CartRepository

class PlusItemInCartUseCase(
    val cartRepository: CartRepository
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