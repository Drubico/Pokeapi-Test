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
import com.drubico.pokeapi.data.local.PokemonTypesDB
import com.drubico.pokeapi.data.local.PokemonTypesDB.pokemonTypeList
import com.drubico.pokeapi.ui.dialog.ToastType
import com.drubico.pokeapi.ui.dialog.toastMessageCustom
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
    private lateinit var loadingAnimationLottie: LottieAnimationView
    private lateinit var buttonGetMorePokemon: Button
    private lateinit var linearLayoutTypes: LinearLayout
    private lateinit var filters: HorizontalScrollView
    private lateinit var appliedFiltersScroll: HorizontalScrollView
    private lateinit var linearLayoutAppliedFilters: LinearLayout
    private lateinit var failImage: ImageView
    private lateinit var failText: TextView
    private lateinit var tvNetworkError: TextView
    private var currentFilter: PokemonTypesDB.PokemonTypeEntity = pokemonTypeList[0]
    private var appliedFilters: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPokemons(requireContext())
        viewModel.getPokemonsFromDb()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pokemon_list, container, false)
        recyclerView = view.findViewById(R.id.rv_listPokemon)
        loadingAnimationLottie = view.findViewById(R.id.loading_animation)
        buttonGetMorePokemon = view.findViewById(R.id.btn_more_pokemon)
        linearLayoutTypes = view.findViewById(R.id.linearLayoutTypes)
        filters = view.findViewById(R.id.filters)
        appliedFiltersScroll = view.findViewById(R.id.appliedFiltersScroll)
        linearLayoutAppliedFilters = view.findViewById(R.id.linearLayoutAppliedFilters)
        failImage = view.findViewById(R.id.fail_img)
        failText = view.findViewById(R.id.fail_text)
        tvNetworkError = view.findViewById(R.id.tv_error_network)
        buttonGetMorePokemon.setOnClickListener {
            viewModel.getPokemons(requireContext())
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
        filterPokemonType()
        rvAddOnScrollListener()

        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            if (!pokemonList.isNullOrEmpty()) {
                adapter.updatePokemonList(pokemonList)
                filterPokemonList(currentFilter.name)
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

        viewModel.isNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) {
                tvNetworkError.visibility = View.VISIBLE
                toastMessageCustom("Hubo un error de red, intentelo de nuevo.", ToastType.ERROR)
                if (viewModel.pokemonList.value?.isEmpty() == true) {
                    failImage.visibility = View.VISIBLE
                    failText.visibility = View.VISIBLE
                    buttonGetMorePokemon.visibility = View.VISIBLE
                }
            } else {
                tvNetworkError.visibility = View.GONE
                failImage.visibility = View.GONE
                failText.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                loadingAnimationLottie.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                filters.visibility = View.GONE
                buttonGetMorePokemon.visibility = View.GONE
            } else {
                loadingAnimationLottie.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                filters.visibility = View.VISIBLE
            }
        }

        viewModel.newItemsCount.observe(viewLifecycleOwner) { newItemsCount ->
            if (newItemsCount > 0) {
                loadingAnimationLottie.visibility = View.GONE
                failImage.visibility = View.GONE
                failText.visibility = View.GONE
                toastMessageCustom(
                    "Se guardaron $newItemsCount nuevos pokemon!.",
                    ToastType.SUCCESS
                )
            }
        }

    }

    private fun filterPokemonList(query: String) {
        adapter.filter.filter(query)
        updateAppliedFilters()
    }

    private fun rvAddOnScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (!adapter.isListFiltered()) {
                    if (totalItemCount <= (lastVisibleItem + 2))
                        buttonGetMorePokemon.visibility = View.VISIBLE
                    else
                        buttonGetMorePokemon.visibility = View.GONE
                }
                if (adapter.isListFiltered()) {
                    buttonGetMorePokemon.visibility = View.GONE
                }
            }
        })
    }

    private fun filterPokemonType() {
        pokemonTypeList.forEach { type ->
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
                currentFilter = type
                filterPokemonList(type.name)
            }
            linearLayoutTypes.addView(button)
        }
    }

    private fun updateAppliedFilters() {
        linearLayoutAppliedFilters.removeAllViews()
        if (currentFilter!=pokemonTypeList[0]) {
            appliedFiltersScroll.visibility = View.VISIBLE
            val filterButton = Button(requireContext())
            filterButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                marginStart = 10
                marginEnd = 10
                gravity = Gravity.CENTER_VERTICAL
            }
            filterButton.setPadding(10, 10, 10, 10)
            filterButton.text = currentFilter.nameDisplay
            filterButton.setBackgroundColor(Color.parseColor(currentFilter.color))
            filterButton.setTextColor(Color.WHITE)
            filterButton.setOnClickListener {
            }
            linearLayoutAppliedFilters.addView(filterButton)

            val clearButton = Button(requireContext())
            clearButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                marginStart = 10
                marginEnd = 10
                gravity = Gravity.CENTER_VERTICAL
            }
            clearButton.setPadding(10, 10, 10, 10)
            clearButton.text = "Limpiar Filtros"
            clearButton.setBackgroundColor(Color.RED)
            clearButton.setTextColor(Color.WHITE)
            clearButton.setOnClickListener {
                currentFilter = pokemonTypeList[0]
                filterPokemonList("")
            }
            linearLayoutAppliedFilters.addView(clearButton)
        } else {
            appliedFiltersScroll.visibility = View.GONE
        }
    }
}
