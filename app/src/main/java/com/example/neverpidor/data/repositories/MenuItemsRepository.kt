package com.example.neverpidor.data.repositories

import com.example.neverpidor.data.database.BeersDao
import com.example.neverpidor.domain.model.DomainBeer
import com.example.neverpidor.domain.model.DomainSnack
import com.example.neverpidor.data.database.entities.BeerEntity
import com.example.neverpidor.data.database.entities.SnackEntity
import com.example.neverpidor.util.mapper.BeerMapper
import com.example.neverpidor.util.mapper.SnackMapper
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.network.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MenuItemsRepositoryImpl @Inject constructor(
    private val beerMapper: BeerMapper,
    private val snackMapper: SnackMapper,
    private val apiClient: ApiClient,
    private val beersDao: BeersDao
): com.example.neverpidor.domain.repository.MenuItemsRepository {

    override fun getDatabaseBeers(): Flow<List<DomainBeer>> {
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

    override fun getDatabaseSnacks(): Flow<List<DomainSnack>> {
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

    override suspend fun getBeerById(beerId: String): DomainBeer {
        val request = beersDao.getBeerById(beerId)
        return beerMapper.buildDomainFromEntity(request)
    }

    override suspend fun getSnackById(snackId: String): DomainSnack {
        val request = beersDao.getSnackById(snackId)
        return snackMapper.buildDomainFromEntity(request)
    }


    override suspend fun addApiBeer(beerRequest: BeerRequest): BeerResponse? {

        val request = apiClient.addBeer(beerRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun addBeerToDatabase(beerResponse: BeerResponse) {
        beersDao.addOneBeer(beerMapper.buildEntityFromResponse(beerResponse))
    }

    override suspend fun addApiSnack(snackRequest: SnackRequest): SnackResponse? {
        val request = apiClient.addSnack(snackRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun addSnackToDatabase(snackResponse: SnackResponse) {
        beersDao.addOneSnack(snackMapper.buildEntityFromResponse(snackResponse))
    }

    override suspend fun deleteApiBeer(beerId: String): BeerResponse? {

        val request = apiClient.deleteBeer(beerId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun deleteBeerFromDatabase(beerId: String) {
        beersDao.deleteBeerById(beerId)
    }

    override suspend fun deleteApiSnack(snackId: String): SnackResponse? {
        val request = apiClient.deleteSnack(snackId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun deleteSnackFromDatabase(snackId: String) {
        beersDao.deleteSnackById(snackId)
    }

    override suspend fun updateApiBeer(beerId: String, beerRequest: BeerRequest): BeerResponse? {

        val request = apiClient.updateBeer(beerId, beerRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun updateDatabaseBeer(beerId: String, beerRequest: BeerRequest) {
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

    override suspend fun updateDatabaseBeer(beerEntity: BeerEntity) {
        beersDao.updateBeer(beerEntity)
    }

    override suspend fun updateApiSnack(snackId: String, snackRequest: SnackRequest): SnackResponse? {
        val request = apiClient.updateSnack(snackId, snackRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun updateDatabaseSnack(snackId: String, snackRequest: SnackRequest) {
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

    override suspend fun updateDatabaseSnack(snackEntity: SnackEntity) {
        beersDao.updateSnack(snack = snackEntity)
    }
}