package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.data.database.entities.UserAndItemsInCart
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.domain.repositories.CartRepository
import kotlinx.coroutines.flow.Flow

class GetUserCartFlowUseCase(
    val repository: CartRepository
) {
     operator fun invoke(user: UserEntity): Flow<UserAndItemsInCart> {
      return  repository.getUserCartByNumber(user.phoneNumber)
    }
}