package com.drubico.pokeapi.ui.pokemonSearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drubico.pokeapi.R
import com.drubico.pokeapi.ui.model.PokemonModel

class PokemonSearchAdapter(private var pokemonList: MutableList<PokemonModel>) : RecyclerView.Adapter<PokemonSearchAdapter.PokemonSearchViewHolder>(), Filterable {


    private var filteredPokemonList = pokemonList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonSearchViewHolder, position: Int) {
        val pokemon = filteredPokemonList[position]
        holder.pokemonNumberTextView.text = pokemon.id.toString()
        holder.pokemonNameTextView.text = pokemon.name
        Glide.with(holder.pokemonImageView.context)
            .load(pokemon.image)
            .into(holder.pokemonImageView)
    }

    override fun getItemCount(): Int {
        return filteredPokemonList.size
    }

    fun updatePokemonList(newPokemonList: List<PokemonModel>) {
        pokemonList.clear()
        pokemonList.addAll(newPokemonList)
        filter.filter("") // Apply current filter to refresh the filtered list
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim() ?: ""

                val filteredList = if (query.isEmpty()) {
                    pokemonList
                } else {
                    pokemonList.filter {
                        it.name.lowercase().contains(query)
                    }
                }

                return FilterResults().apply { values = filteredList }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredPokemonList = results?.values as? MutableList<PokemonModel> ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    inner class PokemonSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonNumberTextView: TextView = itemView.findViewById(R.id.textViewPokemonNumber)
        val pokemonImageView: ImageView = itemView.findViewById(R.id.imageViewPokemon)
        val pokemonNameTextView: TextView = itemView.findViewById(R.id.textViewPokemonName)
    }
}
