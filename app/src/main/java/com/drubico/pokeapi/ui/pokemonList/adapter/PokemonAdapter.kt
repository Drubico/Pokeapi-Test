package com.drubico.pokeapi.ui.pokemonList.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drubico.pokeapi.R
import com.drubico.pokeapi.ui.model.PokemonModel
import com.drubico.pokeapi.ui.pokemonList.PokemonListViewModel

class PokemonAdapter(
    private var pokemonList: MutableList<PokemonModel>,
    private val viewModel: PokemonListViewModel
) :
    RecyclerView.Adapter<PokemonViewHolder>(), Filterable {

    private var filteredPokemonList: MutableList<PokemonModel> = pokemonList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = filteredPokemonList[position]
        holder.cardPokemon.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(pokemon.color))
        holder.pokemonNameTextView.text = pokemon.name
        holder.pokemonNumberTextView.text = "#${pokemon.id}"
        Glide.with(holder.pokemonImageView.context)
            .load(pokemon.image)
            .into(holder.pokemonImageView)
    }

    override fun getItemCount(): Int {
        return filteredPokemonList.size
    }

    fun updatePokemonList(newPokemonList: List<PokemonModel>) {
        val oldSize = pokemonList.size
        val newSize = newPokemonList.size

        pokemonList.clear()
        pokemonList.addAll(newPokemonList)

        filteredPokemonList.clear()
        filteredPokemonList.addAll(newPokemonList)

        if (newSize > oldSize) {
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        } else if (newSize < oldSize) {
            notifyItemRangeRemoved(newSize, oldSize - newSize)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredPokemonList =
                    if (charString.isEmpty() || charString.equals("clear", ignoreCase = true)) {
                        pokemonList.toMutableList()
                    } else {
                        val filterPattern = charString.lowercase().trim()
                        pokemonList.filter {
                            it.type.contains(filterPattern, true)
                        }.toMutableList()
                    }
                val filterResults = FilterResults()
                filterResults.values = filteredPokemonList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredPokemonList = results?.values as MutableList<PokemonModel>
                viewModel.updateListEmptyState(filteredPokemonList.isEmpty())
                notifyDataSetChanged() // notifyDataSetChanged is acceptable here as it's difficult to determine the exact changes in a filtered list
            }
        }
    }
}
