package com.example.beers_app.domain.use_cases.likes

class LikesUseCases(
    val getLikesUseCase: GetLikesUseCase,
    val likeOrDislikeUseCase: LikeOrDislikeUseCase,
    val isItemLikedUseCase: IsItemLikedUseCase,
    val getItemLikesByIdUseCase: GetItemLikesByIdUseCase
)