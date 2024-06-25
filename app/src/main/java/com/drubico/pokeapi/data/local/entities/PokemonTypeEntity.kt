package com.drubico.pokeapi.data.local.entities

data class PokemonTypeEntity(
    val id: Int,
    val name: String,
    val url: String,
    val color: String,
    val nameDisplay: String
)

val pokemonTypes = listOf(
    PokemonTypeEntity(0, "clear", "", "#000000", "Limpiar filtros"),
    PokemonTypeEntity(12, "grass", "https://pokeapi.co/api/v2/type/12/", "#7AC74C", "planta"),
    PokemonTypeEntity(10, "fire", "https://pokeapi.co/api/v2/type/10/", "#EE8130", "fuego"),
    PokemonTypeEntity(11, "water", "https://pokeapi.co/api/v2/type/11/", "#6390F0", "agua"),
    PokemonTypeEntity(7, "bug", "https://pokeapi.co/api/v2/type/7/", "#A6B91A", "bicho"),
    PokemonTypeEntity(1, "normal", "https://pokeapi.co/api/v2/type/1/", "#A8A77A", "normal"),
    PokemonTypeEntity(4, "poison", "https://pokeapi.co/api/v2/type/4/", "#A33EA1", "veneno"),
    PokemonTypeEntity(5, "ground", "https://pokeapi.co/api/v2/type/5/", "#E2BF65", "tierra"),
    PokemonTypeEntity(13, "electric", "https://pokeapi.co/api/v2/type/13/", "#F7D02C", "eléctrico"),
    PokemonTypeEntity(3, "flying", "https://pokeapi.co/api/v2/type/3/", "#A98FF3", "volador"),
    PokemonTypeEntity(2, "fighting", "https://pokeapi.co/api/v2/type/2/", "#C22E28", "lucha"),
    PokemonTypeEntity(13, "ice", "https://pokeapi.co/api/v2/type/13/", "#9CDA5C", "hielo"),
    PokemonTypeEntity(6, "rock", "https://pokeapi.co/api/v2/type/6/", "#B6A136", "roca"),
    PokemonTypeEntity(8, "ghost", "https://pokeapi.co/api/v2/type/8/", "#735797", "fantasma"),
    PokemonTypeEntity(9, "steel", "https://pokeapi.co/api/v2/type/9/", "#B7B7CE", "acero"),
    PokemonTypeEntity(14, "psychic", "https://pokeapi.co/api/v2/type/14/", "#F95587", "psíquico"),
    PokemonTypeEntity(15, "ice", "https://pokeapi.co/api/v2/type/15/", "#96D9D6", "hielo"),
    PokemonTypeEntity(16, "dragon", "https://pokeapi.co/api/v2/type/16/", "#6F35FC", "dragón"),
    PokemonTypeEntity(17, "dark", "https://pokeapi.co/api/v2/type/17/", "#705746", "siniestro"),
    PokemonTypeEntity(18, "fairy", "https://pokeapi.co/api/v2/type/18/", "#D685AD", "hada"),
    PokemonTypeEntity(19, "stellar", "https://pokeapi.co/api/v2/type/19/", "#FFD700", "estelar"),
    PokemonTypeEntity(10001, "unknown", "https://pokeapi.co/api/v2/type/10001/", "#68A090", "desconocido"),
    PokemonTypeEntity(10002, "shadow", "https://pokeapi.co/api/v2/type/10002/", "#705898", "sombra")
)
