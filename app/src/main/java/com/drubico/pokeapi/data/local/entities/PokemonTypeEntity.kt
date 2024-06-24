package com.drubico.pokeapi.data.local.entities

data class PokemonTypeEntity(
    val id: Int,
    val name: String,
    val url: String,
    val color: String
)

val pokemonTypes = listOf(
    PokemonTypeEntity(1, "normal", "https://pokeapi.co/api/v2/type/1/", "#A8A77A"),
    PokemonTypeEntity(2, "fighting", "https://pokeapi.co/api/v2/type/2/", "#C22E28"),
    PokemonTypeEntity(3, "flying", "https://pokeapi.co/api/v2/type/3/", "#A98FF3"),
    PokemonTypeEntity(4, "poison", "https://pokeapi.co/api/v2/type/4/", "#A33EA1"),
    PokemonTypeEntity(5, "ground", "https://pokeapi.co/api/v2/type/5/", "#E2BF65"),
    PokemonTypeEntity(6, "rock", "https://pokeapi.co/api/v2/type/6/", "#B6A136"),
    PokemonTypeEntity(7, "bug", "https://pokeapi.co/api/v2/type/7/", "#A6B91A"),
    PokemonTypeEntity(8, "ghost", "https://pokeapi.co/api/v2/type/8/", "#735797"),
    PokemonTypeEntity(9, "steel", "https://pokeapi.co/api/v2/type/9/", "#B7B7CE"),
    PokemonTypeEntity(10, "fire", "https://pokeapi.co/api/v2/type/10/", "#EE8130"),
    PokemonTypeEntity(11, "water", "https://pokeapi.co/api/v2/type/11/", "#6390F0"),
    PokemonTypeEntity(12, "grass", "https://pokeapi.co/api/v2/type/12/", "#7AC74C"),
    PokemonTypeEntity(13, "electric", "https://pokeapi.co/api/v2/type/13/", "#F7D02C"),
    PokemonTypeEntity(14, "psychic", "https://pokeapi.co/api/v2/type/14/", "#F95587"),
    PokemonTypeEntity(15, "ice", "https://pokeapi.co/api/v2/type/15/", "#96D9D6"),
    PokemonTypeEntity(16, "dragon", "https://pokeapi.co/api/v2/type/16/", "#6F35FC"),
    PokemonTypeEntity(17, "dark", "https://pokeapi.co/api/v2/type/17/", "#705746"),
    PokemonTypeEntity(18, "fairy", "https://pokeapi.co/api/v2/type/18/", "#D685AD"),
    PokemonTypeEntity(19, "stellar", "https://pokeapi.co/api/v2/type/19/", "#FFD700"),
    PokemonTypeEntity(10001, "unknown", "https://pokeapi.co/api/v2/type/10001/", "#68A090"),
    PokemonTypeEntity(10002, "shadow", "https://pokeapi.co/api/v2/type/10002/", "#705898")
)
