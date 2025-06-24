package co.edu.udea.compumovil.gr07_20251.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.*
import co.edu.udea.compumovil.gr07_20251.lab2.navigation.AppNavigation
import co.edu.udea.compumovil.gr07_20251.lab2.ui.theme.Lab2Theme
import co.edu.udea.compumovil.gr07_20251.lab2.worker.SyncEpisodesWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Programar Worker al abrir la app (cada 12 horas)
        val workRequest = PeriodicWorkRequestBuilder<SyncEpisodesWorker>(12, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "PodcastSync",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        // Jetpack Compose Navigation
        setContent {
            Lab2Theme {
                AppNavigation()
            }
        }
    }
}
