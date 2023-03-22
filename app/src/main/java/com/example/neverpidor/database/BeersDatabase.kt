package com.example.neverpidor.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neverpidor.model.entities.BeerEntity
import com.example.neverpidor.model.entities.SnackEntity

@Database(entities = [BeerEntity::class, SnackEntity::class], version = 1)
abstract class BeersDatabase: RoomDatabase() {

   abstract fun getBeersDao(): BeersDao
}