package com.example.neverpidor.data.database

import androidx.room.*
import com.example.neverpidor.data.database.entities.UserAndLikedItems
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.database.entities.UserMenuItemLikes
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUser(user: UserEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE phoneNumber = :number")
    suspend fun deleteUser(number: String)

    @Query("UPDATE users SET name = :name WHERE phoneNumber = :number")
    suspend fun changeUserName(number: String, name: String)

    @Query("UPDATE users SET salt = :salt, hash = :hash WHERE phoneNumber = :number")
    suspend fun changeUserPassword(number: String, hash: String, salt: String)

    @Query("SELECT * FROM users WHERE phoneNumber = :number")
    suspend fun findUserByNumber(number: String): UserEntity?

   // likes section

    @Transaction
    @Query("SELECT * FROM users WHERE phoneNumber = :number")
    suspend fun getUserLikes(number: String): UserAndLikedItems

    @Transaction
    @Query("SELECT COUNT(UID) FROM likes WHERE UID = :id")
    fun getItemLikes(id: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addLike(userMenuItemJoin: UserMenuItemLikes)

    @Query("DELETE FROM likes WHERE phoneNumber = :number AND UID = :id")
    suspend fun removeLike(number: String, id: String)

    @Query("SELECT * FROM likes WHERE phoneNumber = :number AND UID = :id")
    suspend fun findLike(number: String, id: String): UserMenuItemLikes?
}
