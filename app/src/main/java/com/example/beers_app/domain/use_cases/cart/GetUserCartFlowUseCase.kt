package com.example.beers_app.domain.use_cases.cart

import com.example.beers_app.data.database.entities.UserAndItemsInCart
import com.example.beers_app.domain.repositories.CartRepository
import kotlinx.coroutines.flow.Flow

class GetUserCartFlowUseCase(
    private val repository: CartRepository
) {
     operator fun invoke(number: String): Flow<UserAndItemsInCart> {
      return  repository.getUserCartByNumber(number)
    }
}