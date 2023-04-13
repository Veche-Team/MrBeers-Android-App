package com.example.neverpidor.domain.repository

import com.example.neverpidor.data.database.entities.MenuItemEntity
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import com.example.neverpidor.domain.model.DomainItem
import kotlinx.coroutines.flow.Flow

interface MenuItemsRepository {

    fun getDatabaseMenuItems(): Flow<List<DomainItem>>

    suspend fun getMenuItemById(itemId: String): DomainItem

    suspend fun addApiBeer(beerRequest: BeerRequest): BeerResponse?

    suspend fun addBeerToDatabase(beerResponse: BeerResponse)

    suspend fun addApiSnack(snackRequest: SnackRequest): SnackResponse?

    suspend fun addSnackToDatabase(snackResponse: SnackResponse)

    suspend fun deleteApiBeer(beerId: String): BeerResponse?

    suspend fun deleteMenuItemFromDatabase(itemId: String)

    suspend fun deleteApiSnack(snackId: String): SnackResponse?

    suspend fun updateApiBeer(beerId: String, beerRequest: BeerRequest): BeerResponse?

    suspend fun updateDatabaseBeer(beerId: String, beerRequest: BeerRequest)

    suspend fun updateDatabaseMenuItem(itemEntity: MenuItemEntity)

    suspend fun updateApiSnack(snackId: String, snackRequest: SnackRequest): SnackResponse?

    suspend fun updateDatabaseSnack(snackId: String, snackRequest: SnackRequest)
}