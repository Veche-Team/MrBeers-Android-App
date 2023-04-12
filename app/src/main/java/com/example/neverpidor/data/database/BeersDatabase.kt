package com.example.neverpidor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neverpidor.data.database.entities.*

@Database(
    entities = [BeerEntity::class, SnackEntity::class, UserEntity::class, ItemInfo::class, UserJoinItemInfo::class],
    version = 1
)
abstract class BeersDatabase : RoomDatabase() {

    abstract fun getBeersDao(): BeersDao

    abstract fun getUserDao(): UserDao
}