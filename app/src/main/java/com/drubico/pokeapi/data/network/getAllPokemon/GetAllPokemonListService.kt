package com.drubico.pokeapi.data.network.getAllPokemon

import com.drubico.pokeapi.data.network.ApiResponse
import javax.inject.Inject

class GetAllPokemonListService
@Inject constructor(
    private val getPokemonListClient: GetAllPokemonListClient
) {
    suspend fun getListAllPokemon(): ApiResponse<PokemonResponse> {
        return try {
            val response = getPokemonListClient.getPokemonList()
            ApiResponse.Success(response)
        } catch (e: Exception) {
            ApiResponse.Error(listOf(e.message ?: "Unknown error"))
        }
    }

}