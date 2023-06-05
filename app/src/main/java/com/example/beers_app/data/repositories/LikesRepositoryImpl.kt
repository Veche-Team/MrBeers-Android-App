package com.example.beers_app.data.repositories

import com.example.beers_app.data.database.LikesDao
import com.example.beers_app.data.database.entities.UserMenuItemLikes
import com.example.beers_app.domain.repositories.LikesRepository
import javax.inject.Inject

class LikesRepositoryImpl @Inject constructor(private val likesDao: LikesDao) : LikesRepository {

    override suspend fun addLike(userMenuItemJoin: UserMenuItemLikes) =
        likesDao.addLike(userMenuItemJoin)

    override suspend fun removeLike(number: String, id: String) = likesDao.removeLike(number, id)

    override suspend fun getUserLikes(number: String) = likesDao.getUserLikes(number)

    override suspend fun findLike(number: String, id: String) = likesDao.findLike(number, id)

    override fun getItemLikesById(id: String) = likesDao.getItemLikes(id)
}