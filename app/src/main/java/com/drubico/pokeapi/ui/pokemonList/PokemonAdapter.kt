package com.drubico.pokeapi.ui.pokemonList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drubico.pokeapi.R
import com.drubico.pokeapi.ui.model.PokemonModel

class PokemonAdapter(private val pokemonList: List<PokemonModel>) :
    RecyclerView.Adapter<PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.pokemonNameTextView.text = pokemon.name
        Glide.with(holder.pokemonImageView.context)
            .load(pokemon.image)
            .into(holder.pokemonImageView)

    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }
}
