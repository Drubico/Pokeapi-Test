package com.drubico.pokeapi.data.network.getpokemon

import com.drubico.pokeapi.data.network.ApiResponse
import javax.inject.Inject

class GetPokemonService
@Inject constructor(
    private val getPokemonListClient: GetPokemonClient
) {
    suspend fun getListPokemon(id: Int): ApiResponse<PokemonDetailsResponse> {
        return try {
            val response = getPokemonListClient.getPokemonDetails(id)
            ApiResponse.Success(response)
        } catch (e: Exception) {
            ApiResponse.Error(listOf(e.message ?: "Unknown error"))
        }
    }

}