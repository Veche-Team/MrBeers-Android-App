package com.example.beers_app.domain.use_cases.likes

import com.example.beers_app.domain.repositories.LikesRepository

class GetItemLikesByIdUseCase(
    private val repository: LikesRepository
) {
    operator fun invoke(id: String) = repository.getItemLikesById(id)
}