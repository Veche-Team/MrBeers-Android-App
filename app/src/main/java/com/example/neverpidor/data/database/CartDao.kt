package com.example.neverpidor.data.database

import androidx.room.*
import com.example.neverpidor.data.database.entities.UserAndItemsInCart
import com.example.neverpidor.data.database.entities.UserMenuItemCart
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItemInCart(userMenuItemCart: UserMenuItemCart)

    @Query("DELETE FROM cart WHERE phoneNumber = :number AND UID = :id")
    suspend fun removeItemFromCart(number: String, id: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItemInCart(userMenuItemCart: UserMenuItemCart)

    @Query("SELECT * FROM users WHERE phoneNumber = :number")
     fun getUsersCartByNumber(number: String): Flow<UserAndItemsInCart>

    @Query("SELECT * FROM cart WHERE phoneNumber = :number AND UID = :id")
    suspend fun getItemInCart(number: String, id: String): UserMenuItemCart?

    @Query("DELETE FROM cart WHERE phoneNumber = :number")
    suspend fun clearUserCart(number: String)

}