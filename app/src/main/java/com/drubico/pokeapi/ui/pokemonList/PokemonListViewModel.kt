package com.drubico.pokeapi.ui.pokemonList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drubico.pokeapi.data.network.getPokemonList.Pokemon
import com.drubico.pokeapi.domain.Network.GetPokemonListUseCase
import com.drubico.pokeapi.ui.model.PokemonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel
@Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
) : ViewModel() {
    val pokemons = MutableLiveData<List<PokemonModel>>()
    fun getPokemons() {
        viewModelScope.launch {
            val pokemonList = getPokemonListUseCase(15, 0)
            pokemons.value = pokemonList?.pokemons
        }
    }
}