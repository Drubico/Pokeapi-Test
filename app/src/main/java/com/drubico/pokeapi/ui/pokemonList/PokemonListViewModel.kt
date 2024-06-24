package com.drubico.pokeapi.ui.pokemonList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drubico.pokeapi.core.di.PREFERENCES
import com.drubico.pokeapi.core.di.SharedPreferencesProvider
import com.drubico.pokeapi.domain.network.GetPokemonListUseCase
import com.drubico.pokeapi.ui.model.PokemonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel
@Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) : ViewModel() {
    val pokemons = MutableLiveData<MutableList<PokemonModel>>()
    val isLoading = MutableLiveData<Boolean>()
    private val _isListEmpty = MutableLiveData<Boolean>()
    val isListEmpty: LiveData<Boolean> get() = _isListEmpty
    private var page = 0
    private var limit = 15
    private var isLoadingInternal = false

    init {
        getPokemons()
    }

    fun getPokemons() {
        if (isLoadingInternal) return // Evitar cargar más si ya estamos cargando
        isLoadingInternal = true
        isLoading.postValue(true)
        viewModelScope.launch {
            try {
                println("Saved page: $page, saved limit: $limit")
                val pokemonList = getPokemonListUseCase(offset = page*limit, limit = limit)
                val currentList = pokemons.value ?: mutableListOf()
                val newPokemonList = pokemonList?.pokemons?.filterNot { newPokemon ->
                    currentList.any { it.id == newPokemon.id }
                } ?: emptyList()

                if (newPokemonList.isNotEmpty()) {
                    currentList.addAll(newPokemonList)
                    pokemons.postValue(currentList)
                    page++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingInternal = false
                isLoading.postValue(false)
            }
        }
    }

    fun updateListEmptyState(isEmpty: Boolean) {
        _isListEmpty.value = isEmpty
    }

    fun getPage(): Int = sharedPreferencesProvider.getIntegerValue(PREFERENCES.PAGE)
    fun getLimit(): Int = sharedPreferencesProvider.getIntegerValue(PREFERENCES.LIMIT)
}