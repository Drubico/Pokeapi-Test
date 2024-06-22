package com.drubico.pokeapi.domain.Network

import com.drubico.pokeapi.data.PokemonRepository
import javax.inject.Inject

class GetAllPokemonListUseCase
@Inject
constructor(
    private val repository: PokemonRepository
)
{
    suspend operator fun invoke() = repository.getAllPokemonList()
}