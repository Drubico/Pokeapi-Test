package com.drubico.pokeapi.data.network.getpokemon

data class PokemonDetailsResponse(
    val id: Int,
    val name: String,
    val types: List<TypeSlot>
)

data class TypeSlot(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String,
    val url: String
)