package com.drubico.pokeapi.data

import android.content.Context
import com.drubico.pokeapi.core.di.PREFERENCES
import com.drubico.pokeapi.core.di.SharedPreferencesProvider
import com.drubico.pokeapi.data.local.dao.PokemonDao
import com.drubico.pokeapi.data.local.entities.PokemonEntity
import com.drubico.pokeapi.data.local.entities.PokemonTypeEntity
import com.drubico.pokeapi.data.local.entities.pokemonTypes
import com.drubico.pokeapi.data.network.ApiResponse
import com.drubico.pokeapi.data.network.getPokemonList.GetPokemonListService
import com.drubico.pokeapi.data.network.getpokemon.GetPokemonService
import com.drubico.pokeapi.ui.model.PokemonModel
import com.drubico.pokeapi.utils.downloadAndSaveImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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


    suspend fun getPokemonList(
        context: Context,
        isFromNotification: Boolean = false,
        onFailure: (Boolean) -> Unit = {} // lo ocupo para manejar el caso de falla en un contexto fuera del metodo
    ) {
        val haveNewPokemons = sharedPreferencesProvider.getBool(PREFERENCES.HAVE_NEW_POKEMONS, true)
        if (!haveNewPokemons) return
        val page: Int = sharedPreferencesProvider.getIntegerValue(PREFERENCES.COUNTER_POKEMON, 0)
        val limit = if (isFromNotification) 10 else 15
        var counter = sharedPreferencesProvider.getIntegerValue(PREFERENCES.COUNTER_POKEMON, 0)
        when (val response = getPokemonListService.getListPokemon(counter, limit)) {
            is ApiResponse.Success -> {
                onFailure(false)
                val hasNextPage = response.data.next != null
                val pokemonList = response.data.results.map {
                    val id = it.url.extractPokemonId() ?: 0
                    val type = getPokemonType(id)
                    counter = id
                    PokemonModel(
                        id = id,
                        name = it.name,
                        image = downloadAndSaveImage(context, getPokemonImageUrl(id), "$id.png"),
                        color = type?.color ?: "#000000",
                        type = type?.name ?: "Unknown",
                        typeDisplay = type?.nameDisplay ?: "Desconocido"
                    )
                }

                pokemonDao.insertAll(pokemonList.map { castToEntity(it) })
                sharedPreferencesProvider.setIntegerValue(PREFERENCES.PAGE, page + 1)
                sharedPreferencesProvider.setIntegerValue(PREFERENCES.COUNTER_POKEMON, counter)
                sharedPreferencesProvider.setBool(PREFERENCES.HAVE_NEW_POKEMONS, hasNextPage)
            }

            is ApiResponse.Error -> {
                onFailure(true)
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
