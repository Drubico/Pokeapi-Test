package com.drubico.pokeapi.ui.views.pokemonList

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.drubico.pokeapi.data.local.PokemonTypesDB
import com.drubico.pokeapi.data.local.PokemonTypesDB.pokemonTypeList
import com.drubico.pokeapi.databinding.FragmentPokemonListBinding
import com.drubico.pokeapi.ui.dialog.ToastType
import com.drubico.pokeapi.ui.dialog.toastMessageCustom
import com.drubico.pokeapi.ui.views.pokemonList.adapter.PokemonAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var binding: FragmentPokemonListBinding
    private lateinit var adapter: PokemonAdapter
    private val appliedFilters = mutableListOf<PokemonTypesDB.PokemonTypeEntity>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Permiso concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPokemons(requireContext())
        viewModel.getPokemonsFromDb()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false).apply {
            viewModel = this@PokemonListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        binding.rvListPokemon.layoutManager = GridLayoutManager(context, 2)
        adapter = PokemonAdapter(mutableListOf(), viewModel)
        binding.rvListPokemon.adapter = adapter
        filterPokemonType()

        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            if (!pokemonList.isNullOrEmpty()) {
                adapter.updatePokemonList(pokemonList)
                filterPokemonList()
            }
        }

        viewModel.isListEmpty.observe(viewLifecycleOwner) { isEmpty ->
            binding.failCard.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }

        viewModel.isNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) {
                binding.tvErrorNetwork.visibility = View.VISIBLE
                toastMessageCustom("Hubo un error de red, intentelo de nuevo.", ToastType.ERROR)
                if (viewModel.pokemonList.value?.isEmpty() == true) {
                    binding.failCard.visibility = View.VISIBLE
                    binding.btnMorePokemon.visibility = View.VISIBLE
                }
            } else {
                binding.tvErrorNetwork.visibility = View.GONE
                binding.failCard.visibility = View.GONE
            }
        }

        viewModel.newItemsCount.observe(viewLifecycleOwner) { newItemsCount ->
            if (newItemsCount > 0) {
                binding.loadingAnimation.visibility = View.GONE
                binding.failCard.visibility = View.GONE
                toastMessageCustom(
                    "Se guardaron $newItemsCount nuevos pokemon!.",
                    ToastType.SUCCESS
                )
            }
        }

        binding.btnMorePokemon.setOnClickListener {
            viewModel.getPokemons(requireContext())
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun filterPokemonList() {
        val filterNames = appliedFilters.map { it.name }
        val filterString = filterNames.joinToString(",")
        adapter.filter.filter(filterString)
    }

    private fun filterPokemonType() {
        binding.chipGroupTypes.removeAllViews()
        pokemonTypeList.forEach { type ->
            if (type != pokemonTypeList[0]) {
                val chip = Chip(requireContext()).apply {
                    text = type.nameDisplay
                    chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(type.color))
                    setTextColor(Color.WHITE)
                    setOnClickListener {
                        if (appliedFilters.contains(type)) {
                            appliedFilters.remove(type)
                            binding.chipGroupAppliedFilters.removeView(this)
                        } else {
                            appliedFilters.add(type)
                            addFilterChip(type)
                        }
                        filterPokemonList()
                    }
                }
                binding.chipGroupTypes.addView(chip)
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
                binding.chipGroupAppliedFilters.removeView(this)
                filterPokemonList()
            }
        }
        binding.chipGroupAppliedFilters.addView(filterChip)
        binding.appliedFiltersScroll.visibility = View.VISIBLE
    }
}