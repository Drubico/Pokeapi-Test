package com.drubico.pokeapi.core.utils.initializer

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class WorkManagerInitializer : Initializer<WorkManager>, Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun create(context: Context): WorkManager {
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(context, InitializerEntryPoint::class.java)
        hiltEntryPoint.inject(this)
        WorkManager.initialize(context, workManagerConfiguration)
        return WorkManager.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(DependencyGraphInitializer::class.java)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
