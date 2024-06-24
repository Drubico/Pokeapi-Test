package com.drubico.pokeapi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.drubico.pokeapi.ui.pokemonList.PokemonListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var searchEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        searchEditText = findViewById(R.id.searchEditText)

        val searchButton: FloatingActionButton = findViewById(R.id.searchButton)
        searchButton.setOnClickListener {
            if (searchEditText.visibility == View.GONE) {
                searchEditText.visibility = View.VISIBLE
            } else {
                searchEditText.visibility = View.GONE
                searchEditText.text.clear()
                (navHostFragment.childFragmentManager.fragments[0] as? PokemonListFragment)?.clearSearch()
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (navHostFragment.childFragmentManager.fragments[0] as? PokemonListFragment)?.filterPokemonList(
                    s.toString()
                )
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
