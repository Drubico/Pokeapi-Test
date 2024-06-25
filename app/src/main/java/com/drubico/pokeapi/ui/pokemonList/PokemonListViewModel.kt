package com.drubico.pokeapi.ui.pokemonList

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drubico.pokeapi.core.di.PREFERENCES
import com.drubico.pokeapi.core.di.SharedPreferencesProvider
import com.drubico.pokeapi.domain.network.GetPokemonListDBUseCase
import com.drubico.pokeapi.domain.network.GetPokemonListUseCase
import com.drubico.pokeapi.ui.model.PokemonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel
@Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonListFromDb: GetPokemonListDBUseCase,
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    private val _isListEmpty = MutableLiveData<Boolean>()
    val isListEmpty: LiveData<Boolean> get() = _isListEmpty
    private var isLoadingInternal = false
    private val _pokemonList = MutableLiveData<List<PokemonModel>>()
    val pokemonList: LiveData<List<PokemonModel>> get() = _pokemonList

    fun getPokemonsFromDb() = viewModelScope.launch {
        getPokemonListFromDb().collect { pokemons ->
            _pokemonList.value = pokemons
        }
    }

    fun getPokemons(context: Context) {
        isLoadingInternal = true
        isLoading.postValue(true)
        viewModelScope.launch {
            try {
                getPokemonListUseCase(context)
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
}