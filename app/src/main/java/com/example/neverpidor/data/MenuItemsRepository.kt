package com.example.neverpidor.data

import com.example.neverpidor.database.BeersDao
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.entities.BeerEntity
import com.example.neverpidor.model.entities.SnackEntity
import com.example.neverpidor.model.mapper.BeerMapper
import com.example.neverpidor.model.mapper.SnackMapper
import com.example.neverpidor.model.network.beer.Beer
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.SnackList
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.network.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MenuItemsRepository @Inject constructor(
    private val beerMapper: BeerMapper,
    private val snackMapper: SnackMapper,
    private val apiClient: ApiClient,
    private val beersDao: BeersDao
) {

    suspend fun getBeerById(beerId: String): DomainBeer? {

        val request = apiClient.getBeerById(beerId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return beerMapper.buildDomainFromNetwork(request.body.data)
    }

    suspend fun getSnackById(snackId: String): DomainSnack? {

        val request = apiClient.getSnackById(snackId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return snackMapper.buildDomainFromNetwork(request.body.data)
    }

    suspend fun getSnacks(): SnackList? {

        val request = apiClient.getSnacks()

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun getBeers(): List<DomainBeer>? {

        val request = apiClient.getBeers()

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body.data.map {
            beerMapper.buildDomainFromNetwork(it)
        }
    }

    suspend fun addBeer(beerRequest: BeerRequest): BeerResponse? {

        val request = apiClient.addBeer(beerRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun addSnack(snackRequest: SnackRequest): SnackResponse? {
        val request = apiClient.addSnack(snackRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun deleteBeer(beerId: String): BeerResponse? {

        val request = apiClient.deleteBeer(beerId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun deleteSnack(snackId: String): SnackResponse? {
        val request = apiClient.deleteSnack(snackId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun updateBeer(beerId: String, beerRequest: BeerRequest): BeerResponse? {

        val request = apiClient.updateBeer(beerId, beerRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun updateSnack(snackId: String, snackRequest: SnackRequest): SnackResponse? {
        val request = apiClient.updateSnack(snackId, snackRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }
    suspend fun fillDatabase(beers: List<BeerEntity>) {
        beersDao.addBeers(beers)
    }
    suspend fun fillDatabaseWithSnacks(snacks: List<SnackEntity>) {
        beersDao.addSnacks(snacks)
    }

    fun getDatabaseSnacks(): Flow<List<SnackEntity>> {
       return beersDao.getDatabaseSnacks()
    }
}