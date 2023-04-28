package com.example.neverpidor.data.network

import com.example.neverpidor.data.network.dto.beer.BeerList
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.snack.SnackList
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BeersApiService {

    @GET("snacks")
    suspend fun getSnacks(): Response<SnackList>

    @GET("beverages")
    suspend fun getBeers(): Response<BeerList>

    @POST("beverages/add-beverage")
    suspend fun addBeer(@Body beerRequest: BeerRequest): Response<BeerResponse>

    @POST("snacks/add-snack")
    suspend fun addSnack(@Body snackRequest: SnackRequest): Response<SnackResponse>

    @DELETE("beverages/{beerId}")
    suspend fun deleteBeer(@Path("beerId") beerId: String): Response<BeerResponse>

    @DELETE("snacks/{snackId}")
    suspend fun deleteSnack(@Path("snackId") snackId: String): Response<SnackResponse>

    @PUT("beverages/{beerId}")
    suspend fun updateBeer(
        @Path("beerId") beerId: String,
        @Body beerRequest: BeerRequest
    ): Response<BeerResponse>

    @PUT("snacks/{snackId}")
    suspend fun updateSnack(
        @Path("snackId") snackId: String,
        @Body snackRequest: SnackRequest
    ): Response<SnackResponse>
}