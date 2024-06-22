package com.drubico.pokeapi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.drubico.pokeapi.ui.pokemonFilter.PokemonFilterFragment
import com.drubico.pokeapi.ui.pokemonList.PokemonListFragment
import com.drubico.pokeapi.ui.pokemonSearch.PokemonSearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity
 * Author: Diego Rubi
 **/

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filters -> {
                    replaceFragment(PokemonFilterFragment())
                    true
                }
                R.id.action_all_pokemon -> {
                    replaceFragment(PokemonListFragment())
                    true
                }
                R.id.action_search -> {
                    replaceFragment(PokemonSearchFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.action_all_pokemon
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
