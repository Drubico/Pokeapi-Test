package com.drubico.pokeapi.ui.pokemonFilter

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drubico.pokeapi.R

class PokemonFilterFragment : Fragment() {

    companion object {
        fun newInstance() = PokemonFilterFragment()
    }

    private val viewModel: PokemonFilterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pokemon_filter, container, false)
    }
}