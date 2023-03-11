package com.example.neverpidor.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkLayer {

    private const val baseUrl = "http://10.0.2.2:8080/api/"

 //   private const val baseUrl = "https://mr-beers-backend.onrender.com/api/"

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: BeersApiService by lazy { retrofit.create(BeersApiService::class.java) }

    val apiClient = ApiClient(retrofitService)
}