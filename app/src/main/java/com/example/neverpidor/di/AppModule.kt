package com.example.neverpidor.di

import android.content.Context
import androidx.room.Room
import com.example.neverpidor.data.database.BeersDao
import com.example.neverpidor.data.database.BeersDatabase
import com.example.neverpidor.data.database.CartDao
import com.example.neverpidor.data.database.UserDao
import com.example.neverpidor.data.network.ApiClient
import com.example.neverpidor.data.network.BeersApiService
import com.example.neverpidor.data.repositories.CartRepositoryImpl
import com.example.neverpidor.data.repositories.MenuItemsRepositoryImpl
import com.example.neverpidor.data.repositories.UserRepositoryImpl
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.CartRepository
import com.example.neverpidor.domain.repositories.MenuItemsRepository
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.domain.use_cases.cart.*
import com.example.neverpidor.domain.use_cases.likes.*
import com.example.neverpidor.domain.use_cases.menu_items.*
import com.example.neverpidor.domain.use_cases.users.*
import com.example.neverpidor.domain.use_cases.validation.*
import com.example.neverpidor.util.Constants.BASE_URL
import com.example.neverpidor.util.mapper.MenuItemMapper
import com.example.neverpidor.util.security.SecurityUtils
import com.example.neverpidor.util.security.SecurityUtilsImpl
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
        val duration = Duration.ofSeconds(3)
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
    @Singleton
    fun providesBeersDao(beersDatabase: BeersDatabase): BeersDao {
        return beersDatabase.getBeersDao()
    }

    @Provides
    @Singleton
    fun providesUserDao(beersDatabase: BeersDatabase): UserDao {
        return beersDatabase.getUserDao()
    }

    @Provides
    @Singleton
    fun providesCartDao(beersDatabase: BeersDatabase): CartDao {
        return beersDatabase.getCartDao()
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
    fun providesCartRepository(cartDao: CartDao): CartRepository {
        return CartRepositoryImpl(cartDao)
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
            isItemLikedUseCase = IsItemLikedUseCase(appSettings, userRepository),
            getItemLikesByIdUseCase = GetItemLikesByIdUseCase(userRepository)
        )
    }

    @Provides
    @Singleton
    fun providesUserProfileUseCases(
        appSettings: AppSettings,
        userRepository: UserRepository,
        securityUtils: SecurityUtils
    ): UserProfileUseCases {
        return UserProfileUseCases(
            changeUserNameUseCase = ChangeUserNameUseCase(appSettings, userRepository),
            changeUserPasswordUseCase = ChangeUserPasswordUseCase(
                appSettings,
                userRepository,
                securityUtils
            ),
            deleteUserUseCase = DeleteUserUseCase(appSettings, userRepository, securityUtils),
            getUserUseCase = GetUserUseCase(appSettings),
            findUserByNumberUseCase = FindUserByNumberUseCase(userRepository, securityUtils),
            setCurrentUserUseCase = SetCurrentUserUseCase(appSettings),
            registerUserUseCase = RegisterUserUseCase(userRepository, securityUtils),
            addUserListenerUseCase = AddUserListenerUseCase(appSettings)
        )
    }

    @Provides
    @Singleton
    fun providesMenuItemsUseCases(
        menuItemsRepository: MenuItemsRepository
    ): MenuItemsUseCases {
        return MenuItemsUseCases(
            addBeerUseCase = AddBeerUseCase(menuItemsRepository),
            addSnackUseCase = AddSnackUseCase(menuItemsRepository),
            getMenuItemByIdUseCase = GetMenuItemByIdUseCase(menuItemsRepository),
            updateBeerUseCase = UpdateBeerUseCase(menuItemsRepository),
            updateSnackUseCase = UpdateSnackUseCase(menuItemsRepository),
            getAllItemsUseCases = GetAllItemsUseCases(menuItemsRepository),
            deleteItemUseCase = DeleteItemUseCase(menuItemsRepository),
            getItemsSetUseCase = GetItemsSetUseCase()
        )
    }

    @Provides
    @Singleton
    fun providesCartUseCases(
        beerMapper: MenuItemMapper,
        cartRepository: CartRepository
    ): CartUseCases {
        return CartUseCases(
            getCartListUseCase = GetCartListUseCase(beerMapper, cartRepository),
            getUserCartFlowUseCase = GetUserCartFlowUseCase(cartRepository),
            plusItemInCart = PlusItemInCartUseCase(cartRepository),
            minusItemInCart = MinusItemInCartUseCase(cartRepository),
            clearUserCart = ClearUserCartUseCase(cartRepository),
            isItemInCartUseCase = IsItemInCartUseCase(cartRepository),
            addItemToCartUseCase = AddItemToCartUseCase(cartRepository)
        )
    }

    @Provides
    @Singleton
    fun providesSecurityUtils(): SecurityUtils {
        return SecurityUtilsImpl()
    }

    @Provides
    @Singleton
    fun providesValidationUseCases(): ValidationUseCases {
        return ValidationUseCases(
            titleValidationUseCase = TitleValidationUseCase(),
            descriptionValidationUseCase = DescriptionValidationUseCase(),
            typeValidationUseCase = TypeValidationUseCase(),
            priceValidationUseCase = PriceValidationUseCase(),
            alcPercentageValidationUseCase = AlcPercentageValidationUseCase(),
            volumeValidationUseCase = VolumeValidationUseCase()
        )
    }
}