package com.drubico.pokeapi.ui.pokemonList

import android.content.res.ColorStateList
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
import androidx.cardview.widget.CardView
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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
    private lateinit var filters: HorizontalScrollView
    private lateinit var appliedFiltersScroll: HorizontalScrollView
    private lateinit var failCard: MaterialCardView
    private lateinit var tvNetworkError: TextView
    private lateinit var chipGroupTypes: ChipGroup
    private lateinit var chipGroupAppliedFilters: ChipGroup
    private val appliedFilters = mutableListOf<PokemonTypesDB.PokemonTypeEntity>()

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
        chipGroupTypes = view.findViewById(R.id.chipGroupTypes)
        chipGroupAppliedFilters = view.findViewById(R.id.chipGroupAppliedFilters)
        filters = view.findViewById(R.id.filters)
        appliedFiltersScroll = view.findViewById(R.id.appliedFiltersScroll)
        failCard = view.findViewById(R.id.fail_card)
        tvNetworkError = view.findViewById(R.id.tv_error_network)
        buttonGetMorePokemon.setOnClickListener {
            viewModel.getPokemons(requireContext())
            failCard.visibility = View.GONE
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
                filterPokemonList()
            }
        }

        viewModel.isListEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                failCard.visibility = View.VISIBLE
            } else {
                failCard.visibility = View.GONE
            }
        }

        viewModel.isNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) {
                tvNetworkError.visibility = View.VISIBLE
                toastMessageCustom("Hubo un error de red, intentelo de nuevo.", ToastType.ERROR)
                if (viewModel.pokemonList.value?.isEmpty() == true) {
                    failCard.visibility = View.VISIBLE
                    buttonGetMorePokemon.visibility = View.VISIBLE
                }
            } else {
                tvNetworkError.visibility = View.GONE
                failCard.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                loadingAnimationLottie.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                filters.visibility = View.GONE
                buttonGetMorePokemon.visibility = View.GONE
                chipGroupTypes.visibility= View.GONE
                chipGroupAppliedFilters.visibility = View.GONE
            } else {
                loadingAnimationLottie.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                filters.visibility = View.VISIBLE
                chipGroupTypes.visibility= View.VISIBLE
                chipGroupAppliedFilters.visibility = View.VISIBLE
            }
        }

        viewModel.newItemsCount.observe(viewLifecycleOwner) { newItemsCount ->
            if (newItemsCount > 0) {
                loadingAnimationLottie.visibility = View.GONE
                failCard.visibility = View.GONE
                toastMessageCustom(
                    "Se guardaron $newItemsCount nuevos pokemon!.",
                    ToastType.SUCCESS
                )
            }
        }

    }

    private fun filterPokemonList() {
        val filterNames = appliedFilters.map { it.name }
        val filterString = filterNames.joinToString(",")
        adapter.filter.filter(filterString)
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
        chipGroupTypes.removeAllViews()
        pokemonTypeList.forEach { type ->
            if (type != pokemonTypeList[0]) {
                val chip = Chip(requireContext()).apply {
                    text = type.nameDisplay
                    chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(type.color))
                    setTextColor(Color.WHITE)
                    setOnClickListener {
                        if (!appliedFilters.contains(type)) {
                            appliedFilters.add(type)
                            addFilterChip(type)
                            filterPokemonList()
                        }
                    }
                }
                chipGroupTypes.addView(chip)
            }
        }
    }

    private fun addFilterChip(type: PokemonTypesDB.PokemonTypeEntity) {
        val filterChip = Chip(requireContext()).apply {
            text = type.nameDisplay
            chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(type.color))
            setTextColor(Color.WHITE)
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                appliedFilters.remove(type)
                chipGroupAppliedFilters.removeView(this)
                filterPokemonList()
            }
        }
        chipGroupAppliedFilters.addView(filterChip)
        appliedFiltersScroll.visibility = View.VISIBLE
    }


}
