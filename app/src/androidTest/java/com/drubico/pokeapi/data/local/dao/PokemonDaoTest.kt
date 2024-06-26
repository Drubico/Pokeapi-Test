package com.drubico.pokeapi.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.drubico.pokeapi.data.PokeApiDrubicoDatabase
import com.drubico.pokeapi.data.local.entities.PokemonEntity
import com.drubico.pokeapi.data.network.getpokemon.Stat
import com.drubico.pokeapi.data.network.getpokemon.StatSlot
import com.drubico.pokeapi.data.network.getpokemon.Type
import com.drubico.pokeapi.data.network.getpokemon.TypeSlot
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PokemonDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PokeApiDrubicoDatabase
    private lateinit var dao: PokemonDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokeApiDrubicoDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.pokemonDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAll_and_getAllPokemons() = runTest {
        val pokemons = listOf(
            PokemonEntity(
                id = 1,
                name = "Bulbasaur",
                weight = 69,
                height = 7,
                image = "bulbasaur.png",
                types = listOf(TypeSlot(1, Type("Grass", "url_grass")), TypeSlot(2, Type("Poison", "url_poison"))),
                stats = listOf(StatSlot(45, 0, Stat("HP", "url_hp")), StatSlot(49, 0, Stat("Attack", "url_attack")))
            ),
            PokemonEntity(
                id = 2,
                name = "Charmander",
                weight = 85,
                height = 6,
                image = "charmander.png",
                types = listOf(TypeSlot(1, Type("Fire", "url_fire"))),
                stats = listOf(StatSlot(39, 0, Stat("HP", "url_hp")), StatSlot(52, 0, Stat("Attack", "url_attack")))
            )
        )
        dao.insertAll(pokemons)

        val allPokemons = dao.getAllPokemons().first()

        assertThat(allPokemons).containsExactlyElementsIn(pokemons)
    }
}