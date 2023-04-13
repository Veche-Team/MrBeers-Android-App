package com.example.neverpidor.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.neverpidor.data.database.entities.UserEntity

/*
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: UserEntity)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers() : List<UserEntity>
}*/
