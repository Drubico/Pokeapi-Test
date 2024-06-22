package com.drubico.pokeapi.ui.pokemonList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.drubico.pokeapi.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    companion object {
        fun newInstance() = PokemonListFragment()
    }

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingImageView: LottieAnimationView
    private lateinit var adapter: PokemonAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPokemons()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pokemon_list, container, false)
        recyclerView = view.findViewById(R.id.rv_listPokemon)
        loadingImageView = view.findViewById(R.id.loadingAnimation)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = PokemonAdapter(mutableListOf())
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= (lastVisibleItem + 5)) {
                    viewModel.getPokemons()
                }
            }
        })

        viewModel.pokemons.observe(viewLifecycleOwner, Observer { pokemons ->
            if (pokemons.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                loadingImageView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                loadingImageView.visibility = View.GONE
                adapter.updatePokemonList(pokemons)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading && adapter.itemCount == 0) {
                recyclerView.visibility = View.GONE
                loadingImageView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                loadingImageView.visibility = View.GONE
            }
        })
    }
}