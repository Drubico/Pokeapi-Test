package com.drubico.pokeapi.data.network.getAllPokemon

import retrofit2.http.GET
import retrofit2.http.Query

interface GetAllPokemonListClient {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int =0,
        @Query("limit") limit: Int=9999999
    ): PokemonResponse
}