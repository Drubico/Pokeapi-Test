package com.drubico.pokeapi.data

import android.content.Context
import com.drubico.pokeapi.core.utils.ImageUtils.downloadAndSaveImage
import com.drubico.pokeapi.core.utils.PokemonCast.castToEntity
import com.drubico.pokeapi.core.utils.PokemonCast.castToModel
import com.drubico.pokeapi.core.utils.PokemonUrlUtils.extractPokemonId
import com.drubico.pokeapi.core.utils.PokemonUrlUtils.getPokemonImageUrl
import com.drubico.pokeapi.core.utils.sharedPreferences.PREFERENCES
import com.drubico.pokeapi.core.utils.sharedPreferences.SharedPreferencesProvider
import com.drubico.pokeapi.data.local.dao.PokemonDao
import com.drubico.pokeapi.data.local.PokemonTypesDB
import com.drubico.pokeapi.data.local.PokemonTypesDB.pokemonTypeList
import com.drubico.pokeapi.data.network.ApiResponse
import com.drubico.pokeapi.data.network.getPokemonList.GetPokemonListService
import com.drubico.pokeapi.data.network.getpokemon.GetPokemonService
import com.drubico.pokeapi.ui.pokemonList.model.PokemonModel
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


    private suspend fun getPokemonType(pokemonId: Int): PokemonTypesDB.PokemonTypeEntity? {
        when (val response = getPokemonService.getListPokemon(pokemonId)) {
            is ApiResponse.Success -> {
                val types = response.data.types.map { it.type.name }
                val primaryType = types.firstOrNull()
                return pokemonTypeList.find { it.name == primaryType }
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
}
