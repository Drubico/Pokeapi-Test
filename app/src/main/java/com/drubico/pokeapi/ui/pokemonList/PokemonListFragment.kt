package com.drubico.pokeapi.ui.pokemonList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.drubico.pokeapi.R
import com.drubico.pokeapi.ui.dialog.LoadingAlertDialog
import com.drubico.pokeapi.ui.pokemonList.adapter.PokemonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    companion object {
        fun newInstance() = PokemonListFragment()
    }

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private lateinit var loadingDialog : LoadingAlertDialog
    private lateinit var loadingAnimationLottie: LottieAnimationView
    private lateinit var button_more_pokemon: Button

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
        loadingDialog = LoadingAlertDialog(requireContext(),container!!)
        loadingAnimationLottie = view.findViewById(R.id.loading_animation)
        button_more_pokemon = view.findViewById(R.id.btn_more_pokemon)
        button_more_pokemon.setOnClickListener {
            viewModel.getPokemons()
        }
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
                if (totalItemCount <= (lastVisibleItem + 2)) {
                    button_more_pokemon.visibility = View.VISIBLE
                }else
                    button_more_pokemon.visibility = View.GONE
            }
        })

        viewModel.pokemons.observe(viewLifecycleOwner) { pokemons ->
            if (pokemons.isNullOrEmpty()) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
                adapter.updatePokemonList(pokemons)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                recyclerView.visibility = View.GONE
                loadingAnimationLottie.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                loadingAnimationLottie.visibility = View.GONE
            }
        }

    }
    fun filterPokemonList(query: String) {
        adapter.filter.filter(query)
    }

    fun clearSearch() {
        adapter.filter.filter("")
    }

}