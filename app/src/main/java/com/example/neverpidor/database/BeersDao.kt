package com.example.neverpidor.database

import androidx.room.*
import com.example.neverpidor.model.entities.BeerEntity
import com.example.neverpidor.model.entities.SnackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BeersDao {

    //beer

    @Query("SELECT * FROM beers")
     fun getDatabaseBeers(): Flow<List<BeerEntity>>

    @Query("SELECT * FROM beers WHERE UID = :id")
    suspend fun getBeerById(id: String): BeerEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBeers(beers: List<BeerEntity>)

    @Query("DELETE FROM beers WHERE UID = :beerId")
    suspend fun deleteBeerById(beerId: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addOneBeer(beer: BeerEntity)

    @Update
    suspend fun updateBeer(beer: BeerEntity)
    //snacks

    @Query("SELECT * FROM snacks")
    fun getDatabaseSnacks(): Flow<List<SnackEntity>>

    @Query("SELECT * FROM snacks WHERE UID = :id")
    suspend fun getSnackById(id: String): SnackEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSnacks(snacks: List<SnackEntity>)

    @Query("DELETE FROM snacks WHERE UID = :snackId")
    suspend fun deleteSnackById(snackId: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addOneSnack(snack: SnackEntity)

    @Update
    suspend fun updateSnack(snack: SnackEntity)

}