package com.example.neverpidor.domain.use_cases

class LikesUseCases(
    val getLikesUseCase: GetLikesUseCase,
    val likeOrDislikeUseCase: LikeOrDislikeUseCase,
    val isItemLikedUseCase: IsItemLikedUseCase
)