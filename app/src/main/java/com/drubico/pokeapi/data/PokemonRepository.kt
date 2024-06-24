package com.drubico.pokeapi.data

import com.drubico.pokeapi.core.di.PREFERENCES
import com.drubico.pokeapi.core.di.SharedPreferencesProvider
import com.drubico.pokeapi.data.local.dao.PokemonDao
import com.drubico.pokeapi.data.local.entities.PokemonEntity
import com.drubico.pokeapi.data.local.entities.PokemonTypeEntity
import com.drubico.pokeapi.data.local.entities.pokemonTypes
import com.drubico.pokeapi.data.network.ApiResponse
import com.drubico.pokeapi.data.network.getPokemonList.GetPokemonListService
import com.drubico.pokeapi.data.network.getpokemon.GetPokemonService
import com.drubico.pokeapi.ui.model.PokemonListModel
import com.drubico.pokeapi.ui.model.PokemonModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.net.URL
import javax.inject.Inject

class PokemonRepository
@Inject constructor(
    private val getPokemonListService: GetPokemonListService,
    private val getPokemonService: GetPokemonService,
    private val pokemonDao: PokemonDao,
    private val sharedPreferencesProvider: SharedPreferencesProvider,
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


    suspend fun getPokemonList() {
        val page: Int = sharedPreferencesProvider.getIntegerValue(PREFERENCES.PAGE, 0)
        val limit: Int = sharedPreferencesProvider.getIntegerValue(PREFERENCES.LIMIT, 15)
        val nextOffset: Int = sharedPreferencesProvider.getIntegerValue(PREFERENCES.NEXT_OFFSET, page*limit)

        when (val response = getPokemonListService.getListPokemon(nextOffset, limit)) {
            is ApiResponse.Success -> {
                val pokemonList = response.data.results.map {
                    val id = it.url.extractPokemonId() ?: 0
                    val type = getPokemonType(id)
                    PokemonModel(
                        id = id,
                        name = it.name,
                        image = getPokemonImageUrl(it.url.extractPokemonId() ?: 1),
                        color = type?.color ?: "#000000",
                        type = type?.name ?: "Unknown",
                        typeDisplay = type?.nameDisplay ?: "Desconocido"
                    )
                }

                pokemonDao.insertAll(pokemonList.map { castToEntity(it) })
                sharedPreferencesProvider.setIntegerValue(PREFERENCES.PAGE, page + 1)
            }
            is ApiResponse.Error -> {
                println(response)
            }
        }
    }




    private fun castToEntity(pokemonModel: PokemonModel) = PokemonEntity(
        id = pokemonModel.id,
        name = pokemonModel.name,
        image = pokemonModel.image,
        color = pokemonModel.color,
        type = pokemonModel.type,
        typeDisplay = pokemonModel.typeDisplay,
    )

    private suspend fun getPokemonType(pokemonId: Int): PokemonTypeEntity? {
        when (val response = getPokemonService.getListPokemon(pokemonId)) {
            is ApiResponse.Success -> {
                val types = response.data.types.map { it.type.name }
                val primaryType = types.firstOrNull()
                return pokemonTypes.find { it.name == primaryType }
            }

            is ApiResponse.Error -> {
                return null
            }
        }
    }

    fun getPokemonListFromDb(): Flow<List<PokemonModel>> {
        return pokemonDao.getAllPokemons().map { entityList ->
            entityList.map { castToModel(it) }
        }
    }
    private fun castToModel(pokemonEntity: PokemonEntity) = PokemonModel(
        id = pokemonEntity.id,
        name = pokemonEntity.name,
        image = pokemonEntity.image,
        color = pokemonEntity.color,
        type = pokemonEntity.type,
        typeDisplay = pokemonEntity.typeDisplay,
    )
}
