package com.drubico.pokeapi.core.utils

import com.drubico.pokeapi.BuildConfig

object PokemonUrlUtils {
    fun String.extractPokemonId(): Int? {
        return try {
            val parts = this.trimEnd('/').split('/')
            parts.last().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPokemonImageUrl(pokemonId: Int) =
        "${BuildConfig.IMAGE_POKEMON_URL}$pokemonId.png"

}