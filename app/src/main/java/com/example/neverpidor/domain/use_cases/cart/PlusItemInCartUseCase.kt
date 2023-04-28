package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.database.entities.UserMenuItemCart
import com.example.neverpidor.domain.repositories.CartRepository

class PlusItemInCartUseCase(
    val cartRepository: CartRepository
) {

    suspend operator fun invoke(user: UserEntity, itemId: String, itemQuantity: Int) {
        cartRepository.updateItemInCart(
            UserMenuItemCart(
                user.phoneNumber,
                itemId,
                itemQuantity + 1
            )
        )
    }
}