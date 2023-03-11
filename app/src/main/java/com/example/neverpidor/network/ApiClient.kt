package com.example.neverpidor.network

import android.util.Log
import com.example.neverpidor.model.network.beer.Beer
import com.example.neverpidor.model.network.beer.BeerList
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.Snack
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.network.snack.SnackList
import com.example.neverpidor.model.network.snack.SnackRequest
import retrofit2.Response

class ApiClient(private val beersApiService: BeersApiService) {

    suspend fun getBeerById(beerId: String): SimpleResponse<Beer> {
        return safeApiCall { beersApiService.getBeerById(beerId) }
    }

    suspend fun getSnackById(snackId: String): SimpleResponse<Snack> {
        return safeApiCall { beersApiService.getSnackById(snackId) }
    }

    suspend fun getSnacks(): SimpleResponse<SnackList> {
        return safeApiCall { beersApiService.getSnacks() }
    }

    suspend fun getBeers(): SimpleResponse<BeerList> {
        return safeApiCall { beersApiService.getBeers() }
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


    suspend fun updateBeer(beerId: String, beerRequest: BeerRequest): SimpleResponse<BeerResponse> {
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