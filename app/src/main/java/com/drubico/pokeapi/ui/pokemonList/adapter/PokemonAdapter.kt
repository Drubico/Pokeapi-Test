package com.drubico.pokeapi.ui.pokemonList.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drubico.pokeapi.R
import com.drubico.pokeapi.core.utils.ImageUtils.displayImageFromPath
import com.drubico.pokeapi.ui.pokemonList.PokemonListViewModel
import com.drubico.pokeapi.ui.pokemonList.model.PokemonModel

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
        holder.pokemonNameTextView.text = pokemon.name
        holder.pokemonNumberTextView.text = "#${pokemon.id}"
        displayImageFromPath(holder.pokemonImageView, pokemon.image)
        holder.typesContainer.removeAllViews()
        for (type in pokemon.types) {
            val typeTextView = TextView(holder.itemView.context).apply {
                text = type.nameDisplay
                setTextColor(Color.WHITE)
                setPadding(16, 8, 16, 8)
                backgroundTintList = ColorStateList.valueOf(Color.parseColor(type.color))
                setBackgroundResource(R.drawable.type_background)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(4, 0, 4, 0)
                }
            }
            holder.typesContainer.addView(typeTextView)
        }
    }

    override fun getItemCount(): Int {
        return filteredPokemonList.size
    }

    fun isListFiltered(): Boolean {
        return filteredPokemonList.size != pokemonList.size
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
                filteredPokemonList = if (constraint.isNullOrEmpty()) {
                    pokemonList.toMutableList()
                } else {
                    val filterStrings = constraint.toString().split(",")
                    pokemonList.filter { pokemon ->
                        pokemon.types.any { type ->
                            filterStrings.any { filter -> type.name.equals(filter, true) }
                        }
                    }.toMutableList()
                }
                val filterResults = FilterResults()
                filterResults.values = filteredPokemonList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredPokemonList = results?.values as MutableList<PokemonModel>
                viewModel.updateListEmptyState(filteredPokemonList.isEmpty())
                notifyDataSetChanged()
            }
        }
    }
}
