package com.example.neverpidor.domain.repository

import com.example.neverpidor.data.database.entities.BeerEntity
import com.example.neverpidor.data.database.entities.SnackEntity
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import com.example.neverpidor.domain.model.DomainBeer
import com.example.neverpidor.domain.model.DomainSnack
import kotlinx.coroutines.flow.Flow

interface MenuItemsRepository {

    fun getDatabaseBeers(): Flow<List<DomainBeer>>

    fun getDatabaseSnacks(): Flow<List<DomainSnack>>

    suspend fun getBeerById(beerId: String): DomainBeer

    suspend fun getSnackById(snackId: String): DomainSnack

    suspend fun addApiBeer(beerRequest: BeerRequest): BeerResponse?

    suspend fun addBeerToDatabase(beerResponse: BeerResponse)

    suspend fun addApiSnack(snackRequest: SnackRequest): SnackResponse?

    suspend fun addSnackToDatabase(snackResponse: SnackResponse)

    suspend fun deleteApiBeer(beerId: String): BeerResponse?

    suspend fun deleteBeerFromDatabase(beerId: String)

    suspend fun deleteApiSnack(snackId: String): SnackResponse?

    suspend fun deleteSnackFromDatabase(snackId: String)

    suspend fun updateApiBeer(beerId: String, beerRequest: BeerRequest): BeerResponse?

    suspend fun updateDatabaseBeer(beerId: String, beerRequest: BeerRequest)

    suspend fun updateDatabaseBeer(beerEntity: BeerEntity)

    suspend fun updateApiSnack(snackId: String, snackRequest: SnackRequest): SnackResponse?

    suspend fun updateDatabaseSnack(snackId: String, snackRequest: SnackRequest)

    suspend fun updateDatabaseSnack(snackEntity: SnackEntity)
}