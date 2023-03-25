package com.example.neverpidor.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkLayer {

    private const val baseUrl = "http://10.0.2.2:8080/api/"

 //   private const val baseUrl = "https://mr-beers-backend.onrender.com/api/"

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
         val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
    }
    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val duration = Duration.ofSeconds(15)
        return OkHttpClient.Builder()
            .connectTimeout(duration)
            .readTimeout(duration)
            .writeTimeout(duration)
            .build()
    }

    @Provides
    @Singleton
    fun providesBeersService(retrofit: Retrofit): BeersApiService {
        return retrofit.create(BeersApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesApiClient(beersApiService: BeersApiService): ApiClient {
        return ApiClient(beersApiService)
    }
}