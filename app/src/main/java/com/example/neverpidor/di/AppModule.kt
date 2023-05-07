package com.example.neverpidor.di

import android.content.Context
import androidx.room.Room
import com.example.neverpidor.data.database.*
import com.example.neverpidor.data.network.ApiClient
import com.example.neverpidor.data.network.BeersApiService
import com.example.neverpidor.data.repositories.CartRepositoryImpl
import com.example.neverpidor.data.repositories.LikesRepositoryImpl
import com.example.neverpidor.data.repositories.MenuItemsRepositoryImpl
import com.example.neverpidor.data.repositories.UserRepositoryImpl
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.CartRepository
import com.example.neverpidor.domain.repositories.LikesRepository
import com.example.neverpidor.domain.repositories.MenuItemsRepository
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.domain.use_cases.cart.*
import com.example.neverpidor.domain.use_cases.likes.*
import com.example.neverpidor.domain.use_cases.menu_items.*
import com.example.neverpidor.domain.use_cases.users.*
import com.example.neverpidor.domain.use_cases.menu_validation.*
import com.example.neverpidor.domain.use_cases.user_validation.NameValidationUseCase
import com.example.neverpidor.domain.use_cases.user_validation.PasswordValidationUseCase
import com.example.neverpidor.domain.use_cases.user_validation.PhoneNumberValidationUseCase
import com.example.neverpidor.domain.use_cases.user_validation.UserValidationUseCases
import com.example.neverpidor.util.Constants.BASE_URL
import com.example.neverpidor.util.Constants.DATABASE_NAME
import com.example.neverpidor.util.Constants.HTTP_CLIENT_TIMEOUT_DURATION
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
        val duration = Duration.ofSeconds(HTTP_CLIENT_TIMEOUT_DURATION)
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
    fun providesLikesDao(beersDatabase: BeersDatabase): LikesDao {
        return beersDatabase.getLikesDao()
    }

    @Provides
    @Singleton
    fun providesCartDao(beersDatabase: BeersDatabase): CartDao {
        return beersDatabase.getCartDao()
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): BeersDatabase {
        return Room.databaseBuilder(context, BeersDatabase::class.java, DATABASE_NAME).build()
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
    fun providesLikesRepository(likesDao: LikesDao): LikesRepository {
        return LikesRepositoryImpl(likesDao)
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
        likesRepository: LikesRepository
    ): LikesUseCases {
        return LikesUseCases(
            getLikesUseCase = GetLikesUseCase(appSettings, likesRepository),
            likeOrDislikeUseCase = LikeOrDislikeUseCase(appSettings, likesRepository),
            isItemLikedUseCase = IsItemLikedUseCase(appSettings, likesRepository),
            getItemLikesByIdUseCase = GetItemLikesByIdUseCase(likesRepository)
        )
    }

    @Provides
    @Singleton
    fun providesUserProfileUseCases(
        appSettings: AppSettings,
        userRepository: UserRepository,
        securityUtils: SecurityUtils,
        @ApplicationContext context: Context
    ): UserProfileUseCases {
        return UserProfileUseCases(
            changeUserNameUseCase = ChangeUserNameUseCase(appSettings, userRepository),
            changeUserPasswordUseCase = ChangeUserPasswordUseCase(
                appSettings,
                userRepository,
                securityUtils,
                context
            ),
            deleteUserUseCase = DeleteUserUseCase(
                appSettings,
                userRepository,
                securityUtils,
                context
            ),
            getUserUseCase = GetUserUseCase(appSettings),
            findUserByNumberUseCase = FindUserByNumberUseCase(
                userRepository,
                securityUtils,
                context
            ),
            setCurrentUserUseCase = SetCurrentUserUseCase(appSettings),
            registerUserUseCase = RegisterUserUseCase(userRepository, securityUtils, context),
            addUserListenerUseCase = AddUserListenerUseCase(appSettings)
        )
    }

    @Provides
    @Singleton
    fun providesMenuItemsUseCases(
        menuItemsRepository: MenuItemsRepository,
        @ApplicationContext context: Context
    ): MenuItemsUseCases {
        return MenuItemsUseCases(
            addBeerUseCase = AddBeerUseCase(menuItemsRepository, context),
            addSnackUseCase = AddSnackUseCase(menuItemsRepository, context),
            getMenuItemByIdUseCase = GetMenuItemByIdUseCase(menuItemsRepository),
            updateBeerUseCase = UpdateBeerUseCase(menuItemsRepository, context),
            updateSnackUseCase = UpdateSnackUseCase(menuItemsRepository, context),
            getAllItemsUseCases = GetAllItemsUseCases(menuItemsRepository),
            deleteItemUseCase = DeleteItemUseCase(menuItemsRepository, context),
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
    fun providesValidationUseCases(
        @ApplicationContext context: Context
    ): MenuValidationUseCases {
        return MenuValidationUseCases(
            titleValidationUseCase = TitleValidationUseCase(context),
            descriptionValidationUseCase = DescriptionValidationUseCase(context),
            typeValidationUseCase = TypeValidationUseCase(context),
            priceValidationUseCase = PriceValidationUseCase(context),
            alcPercentageValidationUseCase = AlcPercentageValidationUseCase(context),
            salePercentageValidationUseCase = SalePercentageValidationUseCase(context),
            weightValidationUseCase = WeightValidationUseCase(context)
        )
    }

    @Provides
    @Singleton
    fun providesUserValidationUseCases(
        @ApplicationContext context: Context
    ): UserValidationUseCases {
        return UserValidationUseCases(
            passwordValidationUseCase = PasswordValidationUseCase(context),
            phoneNumberValidationUseCase = PhoneNumberValidationUseCase(context),
            nameValidationUseCase = NameValidationUseCase(context)
        )
    }
}