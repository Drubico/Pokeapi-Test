package com.drubico.pokeapi.data.converters

import androidx.room.TypeConverter
import com.drubico.pokeapi.data.network.getpokemon.StatSlot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StatSlotConverter {
    @TypeConverter
    fun fromStatSlotList(value: List<StatSlot>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStatSlotList(value: String): List<StatSlot> {
        val listType = object : TypeToken<List<StatSlot>>() {}.type
        return Gson().fromJson(value, listType)
    }
}