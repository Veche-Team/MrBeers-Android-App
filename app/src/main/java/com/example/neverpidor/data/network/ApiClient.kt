package com.example.neverpidor.data.network

import com.example.neverpidor.data.network.dto.beer.BeerList
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.snack.SnackList
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(private val beersApiService: BeersApiService) {

    suspend fun getSnacks(): MyResponse<SnackList> {
        return safeApiCall { beersApiService.getSnacks() }
    }

    suspend fun getBeers(): MyResponse<BeerList> {
        return safeApiCall { beersApiService.getBeers() }
    }

    suspend fun addBeer(beerRequest: BeerRequest): MyResponse<BeerResponse> {
        return safeApiCall { beersApiService.addBeer(beerRequest) }
    }

    suspend fun addSnack(snackRequest: SnackRequest): MyResponse<SnackResponse> {
        return safeApiCall { beersApiService.addSnack(snackRequest) }
    }

    suspend fun deleteBeer(beerId: String): MyResponse<BeerResponse> {
        return safeApiCall { beersApiService.deleteBeer(beerId) }
    }

    suspend fun deleteSnack(snackId: String): MyResponse<SnackResponse> {
        return safeApiCall { beersApiService.deleteSnack(snackId) }
    }

    suspend fun updateBeer(
        beerId: String,
        beerRequest: BeerRequest
    ): MyResponse<BeerResponse> {
        return safeApiCall { beersApiService.updateBeer(beerId, beerRequest) }
    }

    suspend fun updateSnack(
        snackId: String,
        snackRequest: SnackRequest
    ): MyResponse<SnackResponse> {
        return safeApiCall { beersApiService.updateSnack(snackId, snackRequest) }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): MyResponse<T> {
        return try {
            MyResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            MyResponse.failure(e)
        }
    }
}