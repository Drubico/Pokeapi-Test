package com.drubico.pokeapi.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.drubico.pokeapi.R
import com.drubico.pokeapi.core.di.PREFERENCES
import com.drubico.pokeapi.data.PokemonRepository
import com.drubico.pokeapi.core.di.SharedPreferencesProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class PokemonUpdateWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PokemonRepository,
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            repository.getPokemonList(context = context,true){failed ->
                val haveNewPokemons = sharedPreferencesProvider.getBool(PREFERENCES.HAVE_NEW_POKEMONS, true)
                if (!haveNewPokemons) {
                    showNotification("Actualización de Pokémon", "No tienes más Pokémon disponibles para actualizar.")
                    return@getPokemonList
                }
                if (!failed){
                    showNotification("Actualización de Pokémon", "La lista de Pokémon se ha actualizado.")
                    return@getPokemonList
                }else {
                    showNotification("Error al actualizar", "Necesita internet para actualizar la lista de Pokémon. Por favor, intentelo más tarde.")
                    return@getPokemonList
                }
            }
            scheduleNextWork()
            Result.success()
        } catch (e: Exception) {
            scheduleNextWork()
            Result.retry()
        }
    }

    private fun scheduleNextWork() {
        val nextWorkRequest = OneTimeWorkRequestBuilder<PokemonUpdateWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(nextWorkRequest)
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, message: String) {
        val notificationId = 1
        val channelId = "pokemon_update_channel"

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }
}
