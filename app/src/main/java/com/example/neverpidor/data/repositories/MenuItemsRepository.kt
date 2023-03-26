package com.example.neverpidor.data.repositories

import com.example.neverpidor.database.BeersDao
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.entities.BeerEntity
import com.example.neverpidor.model.entities.SnackEntity
import com.example.neverpidor.model.mapper.BeerMapper
import com.example.neverpidor.model.mapper.SnackMapper
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.network.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MenuItemsRepository @Inject constructor(
    private val beerMapper: BeerMapper,
    private val snackMapper: SnackMapper,
    private val apiClient: ApiClient,
    private val beersDao: BeersDao
) {

    fun getDatabaseBeers(): Flow<List<DomainBeer>> {
        return beersDao
            .getDatabaseBeers()
            .map { list ->
                list.map {
                    beerMapper.buildDomainFromEntity(it)
                }
            }
            .onEach {
                if (it.isEmpty()) {
                    getBeers()
                    getSnacks()
                }
            }
    }

    private suspend fun getBeers() {
        apiClient.getBeers().data.map {
            beerMapper.buildEntityFromNetwork(it)
        }.also {
            beersDao.addBeers(it)
        }
    }

    fun getDatabaseSnacks(): Flow<List<DomainSnack>> {
        return beersDao
            .getDatabaseSnacks()
            .map { list ->
                list.map {
                    snackMapper.buildDomainFromEntity(it)
                }
            }
            .onEach {
                if (it.isEmpty()) {
                    getSnacks()
                    getBeers()
                }
            }
    }

    private suspend fun getSnacks() {
        apiClient.getSnacks().data.map {
            snackMapper.buildEntityFromNetwork(it)
        }.also {
            beersDao.addSnacks(it)
        }
    }

    suspend fun getBeerById(beerId: String): DomainBeer {
        val request = beersDao.getBeerById(beerId)
        return beerMapper.buildDomainFromEntity(request)
    }

    suspend fun getSnackById(snackId: String): DomainSnack {
        val request = beersDao.getSnackById(snackId)
        return snackMapper.buildDomainFromEntity(request)
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

    suspend fun addBeerToDatabase(beerResponse: BeerResponse) {
        beersDao.addOneBeer(beerMapper.buildEntityFromResponse(beerResponse))
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

    suspend fun addSnackToDatabase(snackResponse: SnackResponse) {
        beersDao.addOneSnack(snackMapper.buildEntityFromResponse(snackResponse))
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

    suspend fun deleteBeerFromDatabase(beerId: String) {
        beersDao.deleteBeerById(beerId)
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

    suspend fun deleteSnackFromDatabase(snackId: String) {
        beersDao.deleteSnackById(snackId)
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

    suspend fun updateDatabaseBeer(beerId: String, beerRequest: BeerRequest) {
        beersDao.updateBeer(
            beer = BeerEntity(
                UID = beerId,
                description = beerRequest.description,
                name = beerRequest.name,
                price = beerRequest.price,
                type = beerRequest.type,
                alcPercentage = beerRequest.alcPercentage,
                volume = beerRequest.volume
            )
        )
    }

    suspend fun updateDatabaseBeer(beerEntity: BeerEntity) {
        beersDao.updateBeer(beerEntity)
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

    suspend fun updateDatabaseSnack(snackId: String, snackRequest: SnackRequest) {
        beersDao.updateSnack(
            snack = SnackEntity(
                UID = snackId,
                description = snackRequest.description,
                name = snackRequest.name,
                price = snackRequest.price,
                type = snackRequest.type
            )
        )
    }

    suspend fun updateDatabaseSnack(snackEntity: SnackEntity) {
        beersDao.updateSnack(snack = snackEntity)
    }
}