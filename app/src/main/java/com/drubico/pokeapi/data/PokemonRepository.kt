package com.drubico.pokeapi.data

import android.content.Context
import com.drubico.pokeapi.core.utils.ImageUtils.downloadAndSaveImage
import com.drubico.pokeapi.core.utils.PokemonUrlUtils.extractPokemonId
import com.drubico.pokeapi.core.utils.PokemonUrlUtils.getPokemonImageUrl
import com.drubico.pokeapi.core.utils.sharedPreferences.PREFERENCES
import com.drubico.pokeapi.core.utils.sharedPreferences.SharedPreferencesProvider
import com.drubico.pokeapi.data.local.dao.PokemonDao
import com.drubico.pokeapi.data.local.entities.PokemonEntity
import com.drubico.pokeapi.data.network.ApiResponse
import com.drubico.pokeapi.data.network.getPokemonList.GetPokemonListService
import com.drubico.pokeapi.data.network.getpokemon.GetPokemonService
import com.drubico.pokeapi.data.network.getpokemon.PokemonDetailsResponse
import com.drubico.pokeapi.ui.converters.PokemonCast.castToModel
import com.drubico.pokeapi.ui.views.pokemonList.model.PokemonModel
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
                val pokemonList = response.data.results.map { currentPokemon ->
                    val id = currentPokemon.url.extractPokemonId() ?: 0
                    val detailsPokemon = getPokemonDetails(id)
                    counter = id
                    PokemonEntity(
                        id = id,
                        name = detailsPokemon?.name ?: "",
                        height = detailsPokemon?.height ?: 0,
                        weight = detailsPokemon?.weight ?: 0,
                        types = detailsPokemon?.types ?: emptyList(),
                        stats = detailsPokemon?.stats ?: emptyList(),
                        image = downloadAndSaveImage(context, getPokemonImageUrl(id), "$id.png"),
                    )
                }
                insertPokemonDB(pokemonList)
                saveSuccessSharedPreferences(page, counter, hasNextPage)
            }

            is ApiResponse.Error -> {
                onFailure(true)
                println(response)
            }
        }
    }

    private suspend fun insertPokemonDB(pokemonList: List<PokemonEntity>) {
        pokemonDao.insertAll(pokemonList)
    }

    private fun saveSuccessSharedPreferences(page: Int, counter: Int, hasNextPage: Boolean) {
        sharedPreferencesProvider.setIntegerValue(PREFERENCES.PAGE, page + 1)
        sharedPreferencesProvider.setIntegerValue(PREFERENCES.COUNTER_POKEMON, counter)
        sharedPreferencesProvider.setBool(PREFERENCES.HAVE_NEW_POKEMONS, hasNextPage)
    }


    private suspend fun getPokemonDetails(pokemonId: Int): PokemonDetailsResponse? {
        when (val response = getPokemonService.getListPokemon(pokemonId)) {
            is ApiResponse.Success -> {
                val details = response.data
                return details
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
