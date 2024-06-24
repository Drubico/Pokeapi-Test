package com.drubico.pokeapi.data.network.getpokemon

import retrofit2.http.GET
import retrofit2.http.Path

interface GetPokemonClient {
    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(
        @Path("id") id: Int
    ): PokemonDetailsResponse
}