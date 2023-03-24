package com.example.neverpidor.network

import com.example.neverpidor.model.network.beer.BeerList
import com.example.neverpidor.model.network.beer.CreatedBeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BeersApiService {

    @GET("snacks")
    suspend fun getSnacks(): SnackList

    @GET("beverages")
    suspend fun getBeers(): BeerList

    @POST("beverages/add-beverage")
    suspend fun addBeer(@Body beerRequest: BeerRequest): Response<CreatedBeerResponse>

    @POST("snacks/add-snack")
    suspend fun addSnack(@Body snackRequest: SnackRequest): Response<CreatedSnackResponse>

    @DELETE("beverages/{beerId}")
    suspend fun deleteBeer(@Path("beerId") beerId: String): Response<CreatedBeerResponse>

    @DELETE("snacks/{snackId}")
    suspend fun deleteSnack(@Path("snackId") snackId: String): Response<DeletedSnackResponse>

    @PUT("beverages/{beerId}")
    suspend fun updateBeer(
        @Path("beerId") beerId: String,
        @Body beerRequest: BeerRequest
    ): Response<CreatedBeerResponse>

    @PUT("snacks/{snackId}")
    suspend fun updateSnack(
        @Path("snackId") snackId: String,
        @Body snackRequest: SnackRequest
    ): Response<CreatedSnackResponse>
}