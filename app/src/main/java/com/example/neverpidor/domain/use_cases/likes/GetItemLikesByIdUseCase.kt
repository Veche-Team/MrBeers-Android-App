package com.example.neverpidor.domain.use_cases.likes

import com.example.neverpidor.domain.repositories.UserRepository

class GetItemLikesByIdUseCase(
    val repository: UserRepository
) {
    operator fun invoke(id: String) = repository.getItemLikesById(id)
}