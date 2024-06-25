package com.drubico.pokeapi.domain.network

import android.content.Context
import com.drubico.pokeapi.data.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase
@Inject
constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(context:Context) = repository.getPokemonList(context = context)
}