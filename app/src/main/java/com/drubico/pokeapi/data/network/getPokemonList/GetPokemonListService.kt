package com.drubico.pokeapi.data.network.getPokemonList

import com.drubico.pokeapi.data.network.ApiResponse
import javax.inject.Inject

class GetPokemonListService
@Inject constructor(
    private val getPokemonListClient: GetPokemonListClient
) {
    suspend fun getListPokemon(offset: Int, limit: Int): ApiResponse<PokemonResponse> {
        return try {
            val response = getPokemonListClient.getPokemonList(offset, limit)
            ApiResponse.Success(response)
        } catch (e: Exception) {
            ApiResponse.Error(listOf(e.message ?: "Unknown error"))
        }
    }

}