package com.drubico.pokeapi.domain.network

import android.content.Context
import com.drubico.pokeapi.data.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase
@Inject
constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(context: Context, onFailure: (Boolean) -> Unit = {}) =
        repository.getPokemonList(context = context, onFailure = onFailure)
}