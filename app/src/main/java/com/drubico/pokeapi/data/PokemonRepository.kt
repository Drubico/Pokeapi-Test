package com.drubico.pokeapi.data

import com.drubico.pokeapi.data.local.dao.PokemonDao
import com.drubico.pokeapi.data.local.entities.PokemonEntity
import com.drubico.pokeapi.data.network.ApiResponse
import com.drubico.pokeapi.data.network.getPokemonList.GetPokemonListService
import com.drubico.pokeapi.ui.model.PokemonListModel
import com.drubico.pokeapi.ui.model.PokemonModel
import javax.inject.Inject

class PokemonRepository
@Inject constructor(
    private val getPokemonListService: GetPokemonListService,
    private val pokemonDao: PokemonDao,
) {
    private fun String.extractPokemonId(): Int? {
        return try {
            val parts = this.trimEnd('/').split('/')
            parts.last().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getPokemonImageUrl(pokemonId: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
    }

    suspend fun getPokemonList(offset: Int, limit: Int): PokemonListModel? {
        when (val response = getPokemonListService.getListPokemon(offset, limit)) {
            is ApiResponse.Success -> {
                val pokemonList = response.data.results.map {
                    PokemonModel(
                        id = it.url.extractPokemonId() ?: 0,
                        name = it.name,
                        image = getPokemonImageUrl(it.url.extractPokemonId() ?: 1)
                    )
                }
                val results = PokemonListModel(
                    totalPokemons = response.data.count,
                    pokemons = pokemonList
                )

                pokemonDao.insertAll(pokemonList.map { castToEntity(it) })
                return results
            }

            is ApiResponse.Error -> {
                println(response)
            }
        }
        return null
    }


    private fun castToEntity(pokemonModel: PokemonModel) = PokemonEntity(
        id = pokemonModel.id,
        name = pokemonModel.name,
        image = pokemonModel.image
    )

}
