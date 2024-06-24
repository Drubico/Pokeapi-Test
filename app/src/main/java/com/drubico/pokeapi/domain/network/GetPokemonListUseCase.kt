package com.drubico.pokeapi.domain.network

import com.drubico.pokeapi.data.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase
@Inject
constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke() = repository.getPokemonList()
}