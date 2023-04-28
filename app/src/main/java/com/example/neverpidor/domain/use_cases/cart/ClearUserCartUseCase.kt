package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.domain.repositories.CartRepository

class ClearUserCartUseCase(
    val cartRepository: CartRepository
) {
    suspend operator fun invoke(user: UserEntity) {
        cartRepository.clearUserCart(user.phoneNumber)
    }
}