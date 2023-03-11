package com.example.neverpidor.data

import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.mapper.BeerMapper
import com.example.neverpidor.model.network.beer.Beer
import com.example.neverpidor.model.network.beer.BeerList
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.Snack
import com.example.neverpidor.model.network.snack.SnackList
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.network.NetworkLayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MenuItemsRepository {

    private val beerMapper = BeerMapper()

    suspend fun getBeerById(beerId: String): Beer? {

        val request = NetworkLayer.apiClient.getBeerById(beerId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun getSnackById(snackId: String): Snack? {

        val request = NetworkLayer.apiClient.getSnackById(snackId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun getSnacks(): SnackList? {

        val request = NetworkLayer.apiClient.getSnacks()

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun getBeers(): List<DomainBeer>? {

        val request = NetworkLayer.apiClient.getBeers()

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body.data.map {
            beerMapper.buildFrom(it)
        }
    }

    suspend fun addBeer(beerRequest: BeerRequest): BeerResponse? {

        val request = NetworkLayer.apiClient.addBeer(beerRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun addSnack(snackRequest: SnackRequest): SnackResponse? {
        val request = NetworkLayer.apiClient.addSnack(snackRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun deleteBeer(beerId: String): BeerResponse? {

        val request = NetworkLayer.apiClient.deleteBeer(beerId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun deleteSnack(snackId: String): SnackResponse? {
        val request = NetworkLayer.apiClient.deleteSnack(snackId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun updateBeer(beerId: String, beerRequest: BeerRequest): BeerResponse? {

        val request = NetworkLayer.apiClient.updateBeer(beerId, beerRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    suspend fun updateSnack(snackId: String, snackRequest: SnackRequest): SnackResponse? {
        val request = NetworkLayer.apiClient.updateSnack(snackId, snackRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }
}