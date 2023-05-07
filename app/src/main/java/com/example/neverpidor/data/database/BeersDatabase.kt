package com.example.neverpidor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neverpidor.data.database.entities.*

@Database(
    entities = [
        MenuItemEntity::class,
        UserEntity::class,
        UserMenuItemLikes::class,
        UserMenuItemCart::class
    ],
    version = 1
)
abstract class BeersDatabase : RoomDatabase() {

    abstract fun getBeersDao(): BeersDao

    abstract fun getUserDao(): UserDao

    abstract fun getCartDao(): CartDao

    abstract fun getLikesDao(): LikesDao
}