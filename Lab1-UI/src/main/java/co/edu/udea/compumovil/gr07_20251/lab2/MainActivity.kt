package co.edu.udea.compumovil.gr07_20251.lab2

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.work.*
import co.edu.udea.compumovil.gr07_20251.lab2.navigation.AppNavigation
import co.edu.udea.compumovil.gr07_20251.lab2.ui.theme.Lab2Theme
import co.edu.udea.compumovil.gr07_20251.lab2.worker.SyncEpisodesWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permiso de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        // Ejecutar el Worker de prueba
        val testRequest = OneTimeWorkRequestBuilder<SyncEpisodesWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(testRequest)

        // Jetpack Compose Navigation
        setContent {
            Lab2Theme {
                AppNavigation()
            }
        }
    }
}

