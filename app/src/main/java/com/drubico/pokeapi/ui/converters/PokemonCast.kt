package com.drubico.pokeapi.ui.converters

import com.drubico.pokeapi.data.local.PokemonTypesDB
import com.drubico.pokeapi.data.local.PokemonTypesDB.pokemonTypeList
import com.drubico.pokeapi.data.local.entities.PokemonEntity
import com.drubico.pokeapi.data.network.getpokemon.TypeSlot
import com.drubico.pokeapi.ui.pokemonList.model.PokemonModel

object PokemonCast {

    fun castToModel(pokemonEntity: PokemonEntity) = PokemonModel(
        id = pokemonEntity.id,
        name = pokemonEntity.name,
        image = pokemonEntity.image,
        height = pokemonEntity.height,
        weight = pokemonEntity.weight,
        types = pokemonEntity.types.map { castToModel(it) },
        stats = pokemonEntity.stats
    )

    fun castToModel(type: TypeSlot): PokemonTypesDB.PokemonTypeEntity {
        return pokemonTypeList.find { it.name == type.type.name } ?: pokemonTypeList[0]
    }

}
