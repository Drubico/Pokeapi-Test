package com.drubico.pokeapi.ui.pokemonList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drubico.pokeapi.R

class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pokemonNumberTextView: TextView = itemView.findViewById(R.id.textViewPokemonNumber)
    val pokemonImageView: ImageView = itemView.findViewById(R.id.imageViewPokemon)
    val pokemonNameTextView: TextView = itemView.findViewById(R.id.textViewPokemonName)
}
