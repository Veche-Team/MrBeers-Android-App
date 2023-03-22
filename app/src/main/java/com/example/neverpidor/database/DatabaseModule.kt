package com.example.neverpidor.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun providesBeersDao(beersDatabase: BeersDatabase): BeersDao {
        return beersDatabase.getBeersDao()
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): BeersDatabase {
        return Room.databaseBuilder(context, BeersDatabase::class.java, "beers_db").build()
    }
}