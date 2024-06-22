package com.drubico.pokeapi.ui.pokemonSearch

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drubico.pokeapi.R
import com.drubico.pokeapi.ui.dialog.LoadingAlertDialog
import com.drubico.pokeapi.ui.pokemonList.PokemonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonSearchFragment : Fragment() {

    companion object {
        fun newInstance() = PokemonSearchFragment()
    }

    private val viewModel: PokemonSearchViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonSearchAdapter
    private lateinit var loadingDialog: LoadingAlertDialog
    private lateinit var searchView: SearchView
    private lateinit var searchContainer: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pokemon_search, container, false)
        recyclerView = view.findViewById(R.id.rv_listPokemon)
        searchView = view.findViewById(R.id.search_view)
        searchContainer = view.findViewById(R.id.search_container)
        loadingDialog = LoadingAlertDialog(requireContext(), container!!)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PokemonSearchFragment", "ViewModel inyectado: $viewModel")

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = PokemonSearchAdapter(mutableListOf())
        recyclerView.adapter = adapter

        viewModel.pokemons.observe(viewLifecycleOwner) { pokemons ->
            adapter.updatePokemonList(pokemons)
        }

        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            adapter.filter.filter(query)
            if (query.isNotEmpty()) {
                searchView.setQuery(query, false)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        searchContainer.setOnClickListener {
            searchView.isIconified = false
        }

        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        val colorPrimary = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        searchEditText.setTextColor(colorPrimary)
        searchEditText.setHintTextColor(colorPrimary)
    }
}
