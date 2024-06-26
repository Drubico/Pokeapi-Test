package com.drubico.pokeapi.data.converters

import androidx.room.TypeConverter
import com.drubico.pokeapi.data.network.getpokemon.TypeSlot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeSlotConverter {
    @TypeConverter
    fun fromTypeSlotList(value: List<TypeSlot>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toTypeSlotList(value: String): List<TypeSlot> {
        val listType = object : TypeToken<List<TypeSlot>>() {}.type
        return Gson().fromJson(value, listType)
    }
}