package co.edu.udea.compumovil.gr07_20251.lab2.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr07_20251.lab2.data.model.RssEpisode
import co.edu.udea.compumovil.gr07_20251.lab2.data.network.fetchEpisodesFromRss
import kotlinx.coroutines.launch
import android.media.MediaPlayer
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import co.edu.udea.compumovil.gr07_20251.lab2.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import co.edu.udea.compumovil.gr07_20251.lab2.data.model.EpisodePlayerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailScreen(feedUrl: String) {
    var episodes by remember { mutableStateOf<List<RssEpisode>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var isPreparing by remember { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentlyPlayingUrl by remember { mutableStateOf<String?>(null) }
    var progress by remember { mutableStateOf(0f) }
    var duration by remember { mutableStateOf(0) }
    var currentTime by remember { mutableStateOf(0) }
    var progressJob by remember { mutableStateOf<Job?>(null) }
    var isPaused by remember { mutableStateOf(false) }

    LaunchedEffect(feedUrl) {
        scope.launch {
            episodes = fetchEpisodesFromRss(feedUrl)
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.podcast_detail_title)) })
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(episodes) { episode ->
                    EpisodeCard(episode)
                }
            }
        }
    }
}
fun formatTime(ms: Int): String {
    val minutes = (ms / 1000) / 60
    val seconds = (ms / 1000) % 60
    return "%02d:%02d".format(minutes, seconds)
}
@Composable
fun EpisodeCard(episode: RssEpisode) {
    val scope = rememberCoroutineScope()
    var isPreparing by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var progress by remember { mutableStateOf(0f) }
    var duration by remember { mutableStateOf(0) }
    var currentTime by remember { mutableStateOf(0) }
    var progressJob by remember { mutableStateOf<Job?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(episode.title, fontWeight = FontWeight.Bold)
        Text(episode.pubDate, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (isPlaying) {
                mediaPlayer?.pause()
                isPaused = true
                isPlaying = false
                progressJob?.cancel()
            } else {
                if (isPaused) {
                    mediaPlayer?.start()
                    isPlaying = true
                    isPaused = false

                    progressJob = scope.launch {
                        while (mediaPlayer?.isPlaying == true) {
                            val position = mediaPlayer?.currentPosition ?: 0
                            currentTime = position
                            progress = position.toFloat() / (duration.takeIf { it > 0 } ?: 1)
                            delay(500)
                        }
                    }
                } else {
                    try {
                        mediaPlayer?.release()
                        isPreparing = true
                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(episode.audioUrl)
                            setOnPreparedListener {
                                it.start()
                                isPreparing = false
                                isPlaying = true
                                duration = it.duration

                                progressJob = scope.launch {
                                    while (it.isPlaying) {
                                        val position = it.currentPosition
                                        currentTime = position
                                        progress = position.toFloat() / (duration.takeIf { it > 0 } ?: 1)
                                        delay(500)
                                    }
                                }
                            }
                            setOnErrorListener { _, what, extra ->
                                Log.e("Audio", "MediaPlayer error: what=$what, extra=$extra")
                                isPreparing = false
                                true
                            }
                            prepareAsync()
                        }
                    } catch (e: Exception) {
                        Log.e("Audio", "Error al reproducir audio: ${e.message}")
                        isPreparing = false
                    }
                }
            }
        }) {
            if (isPreparing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    when {
                        isPlaying -> stringResource(R.string.stop)
                        isPaused -> "Reanudar"
                        else -> stringResource(R.string.play)
                    }
                )
            }
        }

        if (isPlaying) {
            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            Text(
                "${formatTime(currentTime)} / ${formatTime(duration)}",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
            )
        }
    }
}

