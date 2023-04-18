package com.example.neverpidor.di

import android.content.Context
import androidx.room.Room
import com.example.neverpidor.data.database.BeersDao
import com.example.neverpidor.data.database.BeersDatabase
import com.example.neverpidor.data.database.UserDao
import com.example.neverpidor.data.network.ApiClient
import com.example.neverpidor.data.network.BeersApiService
import com.example.neverpidor.data.repositories.MenuItemsRepositoryImpl
import com.example.neverpidor.data.repositories.UserRepositoryImpl
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.MenuItemsRepository
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.domain.use_cases.GetLikesUseCase
import com.example.neverpidor.domain.use_cases.IsItemLikedUseCase
import com.example.neverpidor.domain.use_cases.LikeOrDislikeUseCase
import com.example.neverpidor.domain.use_cases.LikesUseCases
import com.example.neverpidor.util.Constants.BASE_URL
import com.example.neverpidor.util.mapper.MenuItemMapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val duration = Duration.ofSeconds(15)
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return OkHttpClient.Builder()
            .connectTimeout(duration)
            .readTimeout(duration)
            .writeTimeout(duration)
            .addInterceptor(logger)
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

    @Provides
    fun providesBeersDao(beersDatabase: BeersDatabase): BeersDao {
        return beersDatabase.getBeersDao()
    }

    @Provides
    fun providesUserDao(beersDatabase: BeersDatabase): UserDao {
        return beersDatabase.getUserDao()
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): BeersDatabase {
        return Room.databaseBuilder(context, BeersDatabase::class.java, "beers_db").build()
    }

    @Provides
    @Singleton
    fun providesMenuItemsRepository(
        beerMapper: MenuItemMapper,
        apiClient: ApiClient,
        beersDao: BeersDao
    ): MenuItemsRepository {
        return MenuItemsRepositoryImpl(beerMapper, apiClient, beersDao)
    }

    @Provides
    @Singleton
    fun providesUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun providesLikesUseCases(
        appSettings: AppSettings,
        userRepository: UserRepository
    ): LikesUseCases {
        return LikesUseCases(
            getLikesUseCase = GetLikesUseCase(appSettings, userRepository),
            likeOrDislikeUseCase = LikeOrDislikeUseCase(appSettings, userRepository),
            isItemLikedUseCase = IsItemLikedUseCase(appSettings, userRepository)
        )
    }
}