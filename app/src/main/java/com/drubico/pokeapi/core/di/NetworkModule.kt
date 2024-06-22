package com.drubico.pokeapi.core.di

import com.drubico.pokeapi.BuildConfig
import com.drubico.pokeapi.data.network.getAllPokemon.GetAllPokemonListClient
import com.drubico.pokeapi.data.network.getPokemonList.GetPokemonListClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonList(retrofit: Retrofit): GetPokemonListClient {
        return retrofit.create(GetPokemonListClient::class.java)
    }

    @Provides
    @Singleton
    fun provideAllPokemonList(retrofit: Retrofit): GetAllPokemonListClient {
        return retrofit.create(GetAllPokemonListClient::class.java)
    }

}