package com.drubico.pokeapi.ui.pokemonList

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.drubico.pokeapi.R
import com.drubico.pokeapi.data.local.entities.pokemonTypes
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
    private lateinit var loadingDialog: LoadingAlertDialog
    private lateinit var loadingAnimationLottie: LottieAnimationView
    private lateinit var button_more_pokemon: Button
    private lateinit var linearLayoutTypes: LinearLayout
    private lateinit var filters: HorizontalScrollView
    private lateinit var failImage: ImageView
    private lateinit var failText: TextView

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
        loadingDialog = LoadingAlertDialog(requireContext(), container!!)
        loadingAnimationLottie = view.findViewById(R.id.loading_animation)
        button_more_pokemon = view.findViewById(R.id.btn_more_pokemon)
        linearLayoutTypes = view.findViewById(R.id.linearLayoutTypes)
        filters = view.findViewById(R.id.filters)
        failImage = view.findViewById(R.id.fail_img)
        failText = view.findViewById(R.id.fail_text)
        button_more_pokemon.setOnClickListener {
            viewModel.getPokemons()
            failImage.visibility = View.GONE
            failText.visibility = View.GONE
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = PokemonAdapter(mutableListOf(), viewModel)
        recyclerView.adapter = adapter

        pokemonTypes.forEach { type ->
            val button = Button(requireContext())
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                marginStart = 10
                marginEnd = 10
                gravity = Gravity.CENTER_VERTICAL
            }
            button.setPadding(10, 10, 10, 10)
            button.text = type.nameDisplay
            button.setBackgroundColor(Color.parseColor(type.color))
            button.setTextColor(Color.WHITE)
            button.setOnClickListener {
                filterPokemonList(type.name)
            }
            linearLayoutTypes.addView(button)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (totalItemCount <= (lastVisibleItem + 2)) {
                    button_more_pokemon.visibility = View.VISIBLE
                } else
                    button_more_pokemon.visibility = View.GONE
            }
        })
        viewModel.pokemons.observe(viewLifecycleOwner) { pokemons ->
            if (!pokemons.isNullOrEmpty()) {
                adapter.updatePokemonList(pokemons)
            }
        }
        viewModel.isListEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                failImage.visibility = View.VISIBLE
                failText.visibility = View.VISIBLE
            } else {
                failImage.visibility = View.GONE
                failText.visibility = View.GONE
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                loadingAnimationLottie.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                filters.visibility = View.GONE
                button_more_pokemon.visibility = View.GONE
            } else {
                loadingAnimationLottie.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                filters.visibility = View.VISIBLE
            }
        }

    }

    private fun filterPokemonList(query: String) {
        adapter.filter.filter(query)
    }

}