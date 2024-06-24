package com.drubico.pokeapi.core.di

import com.drubico.pokeapi.BuildConfig
import com.drubico.pokeapi.data.network.getPokemonList.GetPokemonListClient
import com.drubico.pokeapi.data.network.getpokemon.GetPokemonClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
//            .addInterceptor(logging) // Para ver logs de las peticiones HTTP
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
    fun providePokemonDetails(retrofit: Retrofit): GetPokemonClient {
        return retrofit.create(GetPokemonClient::class.java)
    }

}