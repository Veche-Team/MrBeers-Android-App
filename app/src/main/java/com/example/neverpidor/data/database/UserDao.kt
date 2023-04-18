package com.example.neverpidor.data.database

import androidx.room.*
import com.example.neverpidor.data.database.entities.UserAndMenuItems
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.database.entities.UserMenuItemJoin

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUser(user: UserEntity)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers() : List<UserEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

   // likes section

    @Transaction
    @Query("SELECT * FROM users WHERE phoneNumber = :number")
    suspend fun getUserLikes(number: String): UserAndMenuItems

    @Query("SELECT * FROM likes")
    suspend fun getAll(): List<UserMenuItemJoin>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addLike(userMenuItemJoin: UserMenuItemJoin)

    @Query("DELETE FROM likes WHERE phoneNumber = :number AND UID = :id")
    suspend fun removeLike(number: String, id: String)

    @Query("SELECT * FROM likes WHERE phoneNumber = :number AND UID = :id")
    suspend fun findLike(number: String, id: String): UserMenuItemJoin?
}
