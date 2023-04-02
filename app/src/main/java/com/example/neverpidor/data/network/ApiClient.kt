package com.example.neverpidor.data.network

import android.util.Log
import com.example.neverpidor.data.network.dto.beer.BeerList
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.snack.SnackList
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(private val beersApiService: BeersApiService) {

    suspend fun getSnacks(): SnackList {
        return beersApiService.getSnacks()
    }

    suspend fun getBeers(): BeerList {
        return beersApiService.getBeers()
    }

    suspend fun addBeer(beerRequest: BeerRequest): SimpleResponse<BeerResponse> {
        return safeApiCall { beersApiService.addBeer(beerRequest) }
    }

    suspend fun addSnack(snackRequest: SnackRequest): SimpleResponse<SnackResponse> {
        return safeApiCall { beersApiService.addSnack(snackRequest) }
    }

    suspend fun deleteBeer(beerId: String): SimpleResponse<BeerResponse> {
        return safeApiCall { beersApiService.deleteBeer(beerId) }
    }

    suspend fun deleteSnack(snackId: String): SimpleResponse<SnackResponse> {
        return safeApiCall { beersApiService.deleteSnack(snackId) }
    }

    suspend fun updateBeer(
        beerId: String,
        beerRequest: BeerRequest
    ): SimpleResponse<BeerResponse> {
        return safeApiCall { beersApiService.updateBeer(beerId, beerRequest) }
    }

    suspend fun updateSnack(
        snackId: String,
        snackRequest: SnackRequest
    ): SimpleResponse<SnackResponse> {
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