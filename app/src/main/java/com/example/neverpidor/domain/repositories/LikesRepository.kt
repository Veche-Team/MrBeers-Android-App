package com.example.neverpidor.domain.repositories

import com.example.neverpidor.data.database.entities.UserAndLikedItems
import com.example.neverpidor.data.database.entities.UserMenuItemLikes
import kotlinx.coroutines.flow.Flow

interface LikesRepository {

    suspend fun getUserLikes(number: String): UserAndLikedItems

    fun getItemLikesById(id: String): Flow<Int>

    suspend fun addLike(userMenuItemJoin: UserMenuItemLikes)

    suspend fun removeLike(number: String, id: String)

    suspend fun findLike(number: String, id: String): UserMenuItemLikes?
}