package com.example.neverpidor.data.database

import androidx.room.*
import com.example.neverpidor.data.database.entities.UserEntity

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
}
