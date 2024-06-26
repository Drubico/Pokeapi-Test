package com.drubico.pokeapi.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.drubico.pokeapi.data.converters.StatSlotConverter
import com.drubico.pokeapi.data.converters.TypeSlotConverter
import com.drubico.pokeapi.data.local.dao.PokemonDao
import com.drubico.pokeapi.data.local.entities.PokemonEntity

@Database(
    entities = [
        PokemonEntity::class,
    ], version = 1, exportSchema = false
)
@TypeConverters(TypeSlotConverter::class, StatSlotConverter::class)
abstract class PokeApiDrubicoDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}