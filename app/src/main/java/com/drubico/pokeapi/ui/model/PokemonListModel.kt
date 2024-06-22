package com.drubico.pokeapi.ui.model

data class PokemonListModel (
    val totalPokemons: Int =0,
    val pokemons: List<PokemonModel>
)