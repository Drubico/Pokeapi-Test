package com.drubico.pokeapi.core.utils.initializer

import android.content.Context
import androidx.startup.Initializer
import dagger.hilt.android.EntryPointAccessors

class DependencyGraphInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        EntryPointAccessors.fromApplication(context, InitializerEntryPoint::class.java)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
