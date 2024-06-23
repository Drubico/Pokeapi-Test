package com.drubico.pokeapi.core.di

import android.content.Context
import androidx.room.Room
import com.drubico.pokeapi.data.PokeApiDrubicoDatabase
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
    @Singleton
    fun providePokeApiDrubicoDatabase(@ApplicationContext appContext: Context): PokeApiDrubicoDatabase {
        return Room.databaseBuilder(
            appContext,
            PokeApiDrubicoDatabase::class.java,
            "PokeApiDrubicoDatabase"
        ).build()
    }

    @Provides
    fun pokemonDao(database: PokeApiDrubicoDatabase) = database.pokemonDao()

}