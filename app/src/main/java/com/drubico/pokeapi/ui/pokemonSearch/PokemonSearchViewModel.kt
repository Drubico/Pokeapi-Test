package com.drubico.pokeapi.ui.pokemonSearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drubico.pokeapi.domain.Network.GetAllPokemonListUseCase
import com.drubico.pokeapi.ui.model.PokemonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonSearchViewModel
@Inject constructor(
    private val getPokemonListUseCase: GetAllPokemonListUseCase,
) : ViewModel() {
    val pokemons = MutableLiveData<MutableList<PokemonModel>>()
    val isLoading = MutableLiveData<Boolean>()
    private var isLoadingInternal = false

    init {
        getPokemons()
    }

    private fun getPokemons() {
        if (isLoadingInternal) return
        isLoadingInternal = true
        isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val pokemonList = getPokemonListUseCase()
                val currentList = pokemons.value ?: mutableListOf()
                val newPokemonList = pokemonList?.pokemons?.filterNot { newPokemon ->
                    currentList.any { it.id == newPokemon.id }
                } ?: emptyList()

                if (newPokemonList.isNotEmpty()) {
                    currentList.addAll(newPokemonList)
                    pokemons.postValue(currentList) // Usar postValue para evitar problemas de concurrencia
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingInternal = false
                isLoading.postValue(false)
            }
        }
    }
}