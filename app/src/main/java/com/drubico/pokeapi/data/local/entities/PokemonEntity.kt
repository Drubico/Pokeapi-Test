package com.drubico.pokeapi.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.drubico.pokeapi.data.converters.StatSlotConverter
import com.drubico.pokeapi.data.converters.TypeSlotConverter
import com.drubico.pokeapi.data.network.getpokemon.StatSlot
import com.drubico.pokeapi.data.network.getpokemon.TypeSlot

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "types") @TypeConverters(TypeSlotConverter::class) val types: List<TypeSlot>,
    @ColumnInfo(name = "stats") @TypeConverters(StatSlotConverter::class) val stats: List<StatSlot>
)