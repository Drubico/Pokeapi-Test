package com.drubico.pokeapi.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.drubico.pokeapi.R
import com.drubico.pokeapi.core.di.PREFERENCES
import com.drubico.pokeapi.core.di.SharedPreferencesProvider
import com.drubico.pokeapi.data.PokemonRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class PokemonUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PokemonRepository,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            repository.getPokemonList()
            showNotification("Actualización de Pokémon", "La lista de Pokémon se ha actualizado.")
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
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
