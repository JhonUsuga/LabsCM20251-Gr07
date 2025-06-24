package co.edu.udea.compumovil.gr07_20251.lab2.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import co.edu.udea.compumovil.gr07_20251.lab2.data.model.ITunesPodcast
import co.edu.udea.compumovil.gr07_20251.lab2.data.network.ApiClient
import kotlinx.coroutines.launch
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.navigation.NavHostController
import androidx.compose.ui.res.stringResource
import co.edu.udea.compumovil.gr07_20251.lab2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    var feeds by remember { mutableStateOf<List<ITunesPodcast>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = ApiClient.retrofitService.searchITunesPodcasts()
                if (response.isSuccessful) {
                    feeds = response.body()?.results ?: emptyList()
                } else {
                    Log.e("API_FEEDS", "Error en la respuesta: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_FEEDS", "Excepción: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.podcast_list_title), fontWeight = FontWeight.Bold) }
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
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(feeds) { podcast ->
                    FeedCard(podcast) {
                        podcast.feedUrl?.let { url ->
                            navController.navigate("detail/${Uri.encode(url)}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedCard(podcast: ITunesPodcast, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(podcast.collectionName, style = MaterialTheme.typography.titleMedium)
            Image(
                painter = rememberAsyncImagePainter(podcast.artworkUrl600),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "${stringResource(R.string.author)}: ${podcast.artistName ?: "Desconocido"}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

