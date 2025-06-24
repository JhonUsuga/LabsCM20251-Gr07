package co.edu.udea.compumovil.gr07_20251.lab2.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.edu.udea.compumovil.gr07_20251.lab2.data.model.RssEpisode
import co.edu.udea.compumovil.gr07_20251.lab2.data.network.fetchEpisodesFromRss
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import co.edu.udea.compumovil.gr07_20251.lab2.R
import android.app.PendingIntent
import android.content.Intent
import co.edu.udea.compumovil.gr07_20251.lab2.MainActivity

class SyncEpisodesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val feedUrl = "https://feeds.twit.tv/twit.xml" // Usa cualquier feed RSS válido

            val episodes: List<RssEpisode> = fetchEpisodesFromRss(feedUrl)
            val message = applicationContext.getString(R.string.notification_sync_complete, episodes.size)
            showNotification(applicationContext, message)

            Log.d("SyncEpisodesWorker", "Obtenidos ${episodes.size} episodios desde el RSS")

            Result.success()
        } catch (e: Exception) {
            Log.e("SyncEpisodesWorker", "Excepción: ${e.localizedMessage}")
            Result.retry()
        }
    }
}

private fun showNotification(context: Context, message: String) {
    val channelId = "sync_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Sync Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(context.getString(R.string.notification_sync_title))
        .setContentText(message)
        .setSmallIcon(android.R.drawable.ic_popup_sync)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

    notificationManager.notify(1, notification)
}


