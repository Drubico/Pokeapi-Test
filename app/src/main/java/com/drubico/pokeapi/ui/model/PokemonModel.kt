package com.drubico.pokeapi.ui.model

import android.graphics.Color

data class PokemonModel(
    val id: Int = 0,
    val name: String = "",
    val image: String = "",
    val color: String = "000000",
    val type: String = ""
)
