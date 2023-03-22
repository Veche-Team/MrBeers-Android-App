package com.example.neverpidor.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.neverpidor.model.entities.BeerEntity
import com.example.neverpidor.model.entities.SnackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BeersDao {

    @Query("SELECT * FROM beers")
    suspend fun getDatabaseBeers(): List<BeerEntity>

    @Query("SELECT * FROM beers WHERE UID = :id")
    suspend fun getBeerById(id: String): BeerEntity

    @Query("SELECT * FROM snacks")
    fun getDatabaseSnacks(): Flow<List<SnackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBeers(beers: List<BeerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSnacks(snacks: List<SnackEntity>)
}