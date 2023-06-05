package com.example.beers_app.data.database

import androidx.room.*
import com.example.beers_app.data.database.entities.MenuItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BeersDao {

    @Query("SELECT * FROM items")
     fun getDatabaseMenuItems(): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM items WHERE UID = :id")
    suspend fun getMenuItemById(id: String): MenuItemEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMenuItems(items: List<MenuItemEntity>)

    @Query("DELETE FROM items WHERE UID = :itemId")
    suspend fun deleteMenuItemById(itemId: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addOneMenuItem(item: MenuItemEntity)

    @Update
    suspend fun updateMenuItem(item: MenuItemEntity)

}