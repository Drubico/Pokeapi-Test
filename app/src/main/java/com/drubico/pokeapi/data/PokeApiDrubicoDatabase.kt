package com.drubico.pokeapi.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.drubico.pokeapi.data.local.dao.PokemonDao
import com.drubico.pokeapi.data.local.entities.PokemonEntity

@Database(
    entities = [
        PokemonEntity::class,
    ], version = 1, exportSchema = false
)
abstract class PokeApiDrubicoDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}