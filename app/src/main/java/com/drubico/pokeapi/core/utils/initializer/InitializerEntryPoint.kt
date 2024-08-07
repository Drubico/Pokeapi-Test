package com.drubico.pokeapi.core.utils.initializer

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {
    fun inject(workManagerInitializer: WorkManagerInitializer)
}
