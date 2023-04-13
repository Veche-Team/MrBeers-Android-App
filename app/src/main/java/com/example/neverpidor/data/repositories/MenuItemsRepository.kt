package com.example.neverpidor.data.repositories

import com.example.neverpidor.data.database.BeersDao
import com.example.neverpidor.data.database.entities.MenuItemEntity
import com.example.neverpidor.util.mapper.MenuItemMapper
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.network.ApiClient
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MenuItemsRepositoryImpl @Inject constructor(
    private val beerMapper: MenuItemMapper,
    private val apiClient: ApiClient,
    private val beersDao: BeersDao
): com.example.neverpidor.domain.repository.MenuItemsRepository {

    override fun getDatabaseMenuItems(): Flow<List<DomainItem>> {
        return beersDao
            .getDatabaseMenuItems()
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
            beerMapper.buildBeerEntityFromNetwork(it)
        }.also {
            beersDao.addMenuItems(it)
        }
    }

    private suspend fun getSnacks() {
        apiClient.getSnacks().data.map {
            beerMapper.buildSnackEntityFromNetwork(it)
        }.also {
            beersDao.addMenuItems(it)
        }
    }

    override suspend fun getMenuItemById(itemId: String): DomainItem {
        val request = beersDao.getMenuItemById(itemId)
        return beerMapper.buildDomainFromEntity(request)
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
        beersDao.addOneMenuItem(beerMapper.buildBeerEntityFromResponse(beerResponse))
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
        beersDao.addOneMenuItem(beerMapper.buildSnackEntityFromResponse(snackResponse))
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

    override suspend fun deleteMenuItemFromDatabase(itemId: String) {
        beersDao.deleteMenuItemById(itemId)
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
        beersDao.updateMenuItem(
            item = MenuItemEntity(
                UID = beerId,
                description = beerRequest.description,
                name = beerRequest.name,
                price = beerRequest.price,
                type = beerRequest.type,
                alcPercentage = beerRequest.alcPercentage,
                volume = beerRequest.volume,
                category = MenuCategory.BeerCategory
            )
        )
    }

    override suspend fun updateDatabaseMenuItem(itemEntity: MenuItemEntity) {
        beersDao.updateMenuItem(itemEntity)
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
        beersDao.updateMenuItem(
            item = MenuItemEntity(
                UID = snackId,
                description = snackRequest.description,
                name = snackRequest.name,
                price = snackRequest.price,
                type = snackRequest.type,
                category = MenuCategory.SnackCategory,
                alcPercentage = null,
                volume = null
            )
        )
    }
}