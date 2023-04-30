package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.data.database.entities.UserAndItemsInCart
import com.example.neverpidor.domain.repositories.CartRepository
import kotlinx.coroutines.flow.Flow

class GetUserCartFlowUseCase(
    private val repository: CartRepository
) {
     operator fun invoke(number: String): Flow<UserAndItemsInCart> {
      return  repository.getUserCartByNumber(number)
    }
}