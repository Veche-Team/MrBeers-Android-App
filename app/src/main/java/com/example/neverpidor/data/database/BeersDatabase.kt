package com.example.neverpidor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neverpidor.data.database.entities.BeerEntity
import com.example.neverpidor.data.database.entities.SnackEntity

@Database(entities = [BeerEntity::class, SnackEntity::class], version = 1)
abstract class BeersDatabase: RoomDatabase() {

   abstract fun getBeersDao(): BeersDao
}