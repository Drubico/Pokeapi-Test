package com.drubico.pokeapi.ui.pokemonSearch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drubico.pokeapi.domain.Network.GetAllPokemonListUseCase
import com.drubico.pokeapi.ui.model.PokemonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonSearchViewModel @Inject constructor(
    private val getPokemonListUseCase: GetAllPokemonListUseCase
) : ViewModel() {

    val pokemons = MutableLiveData<MutableList<PokemonModel>>()
    val isLoading = MutableLiveData<Boolean>()
    val searchQuery = MutableLiveData<String>()
    private var isDataLoaded = false

    init {
        loadPokemonsIfNeeded()
        Log.d("PokemonSearchViewModel", "ViewModel creado con $getPokemonListUseCase")
    }

    private fun loadPokemonsIfNeeded() {
        if (!isDataLoaded) {
            getPokemons()
            isDataLoaded = true
        }
    }

    private fun getPokemons() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val pokemonList = getPokemonListUseCase()
                val currentList = pokemons.value ?: mutableListOf()
                val newPokemonList = pokemonList?.pokemons?.filterNot { newPokemon ->
                    currentList.any { it.id == newPokemon.id }
                } ?: emptyList()

                if (newPokemonList.isNotEmpty()) {
                    currentList.addAll(newPokemonList)
                    pokemons.value = currentList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
}