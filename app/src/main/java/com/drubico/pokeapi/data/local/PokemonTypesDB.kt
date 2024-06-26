package com.drubico.pokeapi.data.local

object PokemonTypesDB {
    data class PokemonTypeEntity(
        val id: Int,
        val name: String,
        val url: String,
        val color: String,
        val nameDisplay: String
    )
    val pokemonTypeList = listOf(
        PokemonTypeEntity(0, "", "", "#000000", "Todos"),
        PokemonTypeEntity(12, "grass", "https://pokeapi.co/api/v2/type/12/", "#7AC74C", "Planta"),
        PokemonTypeEntity(10, "fire", "https://pokeapi.co/api/v2/type/10/", "#EE8130", "Fuego"),
        PokemonTypeEntity(11, "water", "https://pokeapi.co/api/v2/type/11/", "#6390F0", "Agua"),
        PokemonTypeEntity(7, "bug", "https://pokeapi.co/api/v2/type/7/", "#A6B91A", "Bicho"),
        PokemonTypeEntity(3, "flying", "https://pokeapi.co/api/v2/type/3/", "#A98FF3", "Volador"),
        PokemonTypeEntity(4, "poison", "https://pokeapi.co/api/v2/type/4/", "#A33EA1", "Veneno"),
        PokemonTypeEntity(13, "electric", "https://pokeapi.co/api/v2/type/13/", "#F7D02C", "Eléctrico"),
        PokemonTypeEntity(1, "normal", "https://pokeapi.co/api/v2/type/1/", "#A8A77A", "Normal"),
        PokemonTypeEntity(5, "ground", "https://pokeapi.co/api/v2/type/5/", "#E2BF65", "Tierra"),
        PokemonTypeEntity(6, "rock", "https://pokeapi.co/api/v2/type/6/", "#B6A136", "Roca"),
        PokemonTypeEntity(18, "fairy", "https://pokeapi.co/api/v2/type/18/", "#D685AD", "Hada"),
        PokemonTypeEntity(2, "fighting", "https://pokeapi.co/api/v2/type/2/", "#C22E28", "Lucha"),
        PokemonTypeEntity(8, "ghost", "https://pokeapi.co/api/v2/type/8/", "#735797", "Fantasma"),
        PokemonTypeEntity(14, "psychic", "https://pokeapi.co/api/v2/type/14/", "#F95587", "Psíquico"),
        PokemonTypeEntity(9, "steel", "https://pokeapi.co/api/v2/type/9/", "#B7B7CE", "Acero"),
        PokemonTypeEntity(15, "ice", "https://pokeapi.co/api/v2/type/15/", "#96D9D6", "Hielo"),
        PokemonTypeEntity(16, "dragon", "https://pokeapi.co/api/v2/type/16/", "#6F35FC", "Dragón"),
        PokemonTypeEntity(17, "dark", "https://pokeapi.co/api/v2/type/17/", "#705746", "Siniestro"),
        PokemonTypeEntity(19, "stellar", "https://pokeapi.co/api/v2/type/19/", "#FFD700", "Estelar"),
        PokemonTypeEntity(10001, "unknown", "https://pokeapi.co/api/v2/type/10001/", "#68A090", "Desconocido")
    )

}