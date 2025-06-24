package co.edu.udea.compumovil.gr07_20251.lab2.ui.screens

import android.media.MediaPlayer
import android.text.format.DateFormat
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import co.edu.udea.compumovil.gr07_20251.lab2.data.model.Episode
import co.edu.udea.compumovil.gr07_20251.lab2.data.network.ApiClient
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailScreen(feedId: Int) {
    val scope = rememberCoroutineScope()
    var episodes by remember { mutableStateOf<List<Episode>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(feedId) {
        scope.launch {
            try {
                val response = ApiClient.retrofitService.getEpisodesByFeedId(feedId)
                if (response.isSuccessful) {
                    episodes = response.body()?.items ?: emptyList()
                }
            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Episodios del podcast") }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(episodes) { episode ->
                    EpisodeCard(episode)
                }
            }
        }
    }
}

@Composable
fun EpisodeCard(episode: Episode) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = episode.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = (episode.description ?: "").take(200) + "...",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Publicado: ${formatDate(episode.datePublished)}",
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (!isPlaying) {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(episode.enclosureUrl)
                        prepare()
                        start()
                    }
                    isPlaying = true
                } else {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                    isPlaying = false
                }
            }) {
                Text(if (isPlaying) "Detener" else "Reproducir")
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    return try {
        val date = Date(timestamp * 1000) // La API usa epoch en segundos
        DateFormat.format("dd/MM/yyyy", date).toString()
    } catch (e: Exception) {
        "Fecha desconocida"
    }
}

