package com.example.neverpidor.data

import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.mapper.BeerMapper
import com.example.neverpidor.model.mapper.SnackMapper
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.network.NetworkLayer
import javax.inject.Inject

class MenuItemsRepository @Inject constructor(
    private val beerMapper: BeerMapper,
    private val snackMapper: SnackMapper
) {

    suspend fun getBeerById(beerId: String): DomainBeer? {

        val request = NetworkLayer.apiClient.getBeerById(beerId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return beerMapper.buildFrom(request.body.data)
    }

    suspend fun getSnackById(snackId: String): DomainSnack? {

        val request = NetworkLayer.apiClient.getSnackById(snackId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return snackMapper.buildFrom(request.body.data)
    }

    suspend fun getSnacks(): List<DomainSnack>? {

        val request = NetworkLayer.apiClient.getSnacks()

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body.data.map {
            snackMapper.buildFrom(it)
        }
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