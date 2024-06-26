package com.drubico.pokeapi.ui.pokemonList.model

import com.drubico.pokeapi.data.local.PokemonTypesDB
import com.drubico.pokeapi.data.network.getpokemon.StatSlot

data class PokemonModel(
    val id: Int = 0,
    val name: String = "",
    val image: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val types: List<PokemonTypesDB.PokemonTypeEntity> = emptyList(),
    val stats: List<StatSlot> = emptyList(),
)
