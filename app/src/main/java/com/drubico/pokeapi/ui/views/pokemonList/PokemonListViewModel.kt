package com.drubico.pokeapi.ui.views.pokemonList

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drubico.pokeapi.domain.local.GetPokemonListDBUseCase
import com.drubico.pokeapi.domain.network.GetPokemonListUseCase
import com.drubico.pokeapi.ui.views.pokemonList.model.PokemonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel
@Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonListFromDb: GetPokemonListDBUseCase,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isListEmpty = MutableLiveData<Boolean>()
    val isListEmpty: LiveData<Boolean> get() = _isListEmpty

    private val _pokemonList = MutableLiveData<List<PokemonModel>>()
    val pokemonList: LiveData<List<PokemonModel>> get() = _pokemonList

    private val _isNetworkError = MutableLiveData<Boolean>()
    val isNetworkError: LiveData<Boolean> get() = _isNetworkError

    private var previousPokemonList: List<PokemonModel> = emptyList()

    private val _newItemsCount = MutableLiveData<Int>()
    val newItemsCount: LiveData<Int> get() = _newItemsCount


    fun getPokemonsFromDb() = viewModelScope.launch {
        getPokemonListFromDb().collect { pokemons ->
            updatePokemonList(pokemons)
        }
    }

    fun getPokemons(context: Context) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                getPokemonListUseCase(
                    context
                ) { isFailure ->
                    _isNetworkError.postValue(isFailure)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateListEmptyState(isEmpty: Boolean) {
        _isListEmpty.value = isEmpty
    }

    private fun updatePokemonList(newPokemonList: List<PokemonModel>) {
        val newItems = newPokemonList.size - previousPokemonList.size
        if (newItems > 0) {
            _newItemsCount.value = newItems
        }
        _pokemonList.value = newPokemonList
        previousPokemonList = newPokemonList
    }
}