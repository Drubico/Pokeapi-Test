package com.drubico.pokeapi.data.network.getPokemonList

import retrofit2.http.GET
import retrofit2.http.Query

interface GetPokemonListClient {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonResponse
}