package com.example.neverpidor.network

import android.util.Log
import com.example.neverpidor.model.network.beer.BeerList
import com.example.neverpidor.model.network.beer.CreatedBeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.*
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(private val beersApiService: BeersApiService) {

    suspend fun getSnacks(): SnackList {
        return beersApiService.getSnacks()
    }

    suspend fun getBeers(): BeerList {
        return beersApiService.getBeers()
    }

    suspend fun addBeer(beerRequest: BeerRequest): SimpleResponse<CreatedBeerResponse> {
        return safeApiCall { beersApiService.addBeer(beerRequest) }
    }

    suspend fun addSnack(snackRequest: SnackRequest): SimpleResponse<CreatedSnackResponse> {
        return safeApiCall { beersApiService.addSnack(snackRequest) }
    }

    suspend fun deleteBeer(beerId: String): SimpleResponse<CreatedBeerResponse> {
        return safeApiCall { beersApiService.deleteBeer(beerId) }
    }

    suspend fun deleteSnack(snackId: String): SimpleResponse<DeletedSnackResponse> {
        return safeApiCall { beersApiService.deleteSnack(snackId) }
    }

    suspend fun updateBeer(
        beerId: String,
        beerRequest: BeerRequest
    ): SimpleResponse<CreatedBeerResponse> {
        return safeApiCall { beersApiService.updateBeer(beerId, beerRequest) }
    }

    suspend fun updateSnack(
        snackId: String,
        snackRequest: SnackRequest
    ): SimpleResponse<CreatedSnackResponse> {
        return safeApiCall { beersApiService.updateSnack(snackId, snackRequest) }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
        return try {
            Log.i("ApiClient", "Success")
            SimpleResponse.success(apiCall.invoke())

        } catch (e: Exception) {
            Log.i("ApiClient", "Failure")
            SimpleResponse.failure(e)
        }
    }
}