package com.drubico.pokeapi.core.utils

import com.drubico.pokeapi.data.local.entities.PokemonEntity
import com.drubico.pokeapi.ui.pokemonList.model.PokemonModel

object PokemonCast {
    fun castToEntity(pokemonModel: PokemonModel) = PokemonEntity(
        id = pokemonModel.id,
        name = pokemonModel.name,
        image = pokemonModel.image,
        color = pokemonModel.color,
        type = pokemonModel.type,
        typeDisplay = pokemonModel.typeDisplay,
    )

    fun castToModel(pokemonEntity: PokemonEntity) = PokemonModel(
        id = pokemonEntity.id,
        name = pokemonEntity.name,
        image = pokemonEntity.image,
        color = pokemonEntity.color,
        type = pokemonEntity.type,
        typeDisplay = pokemonEntity.typeDisplay,
    )
}
