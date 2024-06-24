package com.drubico.pokeapi.core.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("Principal", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSharedPreferencesImpl(sharedPreferences: SharedPreferences): SharedPreferencesProvider {
        return SharedPreferencesProvider(sharedPreferences)
    }
}