package com.example.neverpidor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.neverpidor.data.database.entities.*
import com.example.neverpidor.data.database.type_converters.CategoryConverter

@Database(
    entities = [
        MenuItemEntity::class,
        UserEntity::class,
        UserMenuItemJoin::class
    ],
    version = 1
)
abstract class BeersDatabase : RoomDatabase() {

    abstract fun getBeersDao(): BeersDao

      abstract fun getUserDao(): UserDao
}