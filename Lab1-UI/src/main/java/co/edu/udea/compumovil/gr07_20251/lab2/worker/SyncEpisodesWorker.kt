package co.edu.udea.compumovil.gr07_20251.lab2.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.edu.udea.compumovil.gr07_20251.lab2.data.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncEpisodesWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val feedId = 7212558 // ID del feed fijo
            val response = ApiClient.retrofitService.getEpisodesByFeedId(feedId)

            if (response.isSuccessful) {
                val episodes = response.body()?.items ?: emptyList()

                Log.d("SyncEpisodesWorker", "Se obtuvieron ${episodes.size} episodios del feed $feedId")

                // Aquí puedes guardar los episodios en Room o mostrar en la app
                Result.success()
            } else {
                Log.e("SyncEpisodesWorker", "Error: ${response.code()}")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SyncEpisodesWorker", "Excepción: ${e.localizedMessage}")
            Result.retry()
        }
    }
}
