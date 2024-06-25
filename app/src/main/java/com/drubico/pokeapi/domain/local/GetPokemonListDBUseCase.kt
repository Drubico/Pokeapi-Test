package com.drubico.pokeapi.domain.local

import com.drubico.pokeapi.data.PokemonRepository
import javax.inject.Inject

class GetPokemonListDBUseCase
@Inject
constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke() = repository.getPokemonListFromDb()
}