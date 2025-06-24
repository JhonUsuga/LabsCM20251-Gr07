package co.edu.udea.compumovil.gr07_20251.lab2.data.model

data class ITunesPodcast(
    val collectionId: Int,
    val collectionName: String,
    val artworkUrl600: String,
    val feedUrl: String?, // opcional
    val artistName: String?
)