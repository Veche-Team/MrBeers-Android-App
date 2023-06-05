package com.example.beers_app.domain.repositories

import com.example.beers_app.data.database.entities.UserAndLikedItems
import com.example.beers_app.data.database.entities.UserMenuItemLikes
import kotlinx.coroutines.flow.Flow

interface LikesRepository {

    suspend fun getUserLikes(number: String): UserAndLikedItems

    fun getItemLikesById(id: String): Flow<Int>

    suspend fun addLike(userMenuItemJoin: UserMenuItemLikes)

    suspend fun removeLike(number: String, id: String)

    suspend fun findLike(number: String, id: String): UserMenuItemLikes?
}