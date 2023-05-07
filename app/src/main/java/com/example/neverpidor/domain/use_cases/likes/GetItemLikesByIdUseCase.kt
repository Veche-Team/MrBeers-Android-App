package com.example.neverpidor.domain.use_cases.likes

import com.example.neverpidor.domain.repositories.LikesRepository

class GetItemLikesByIdUseCase(
    private val repository: LikesRepository
) {
    operator fun invoke(id: String) = repository.getItemLikesById(id)
}