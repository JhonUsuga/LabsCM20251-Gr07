package co.edu.udea.compumovil.gr07_20251.lab2.data.model

data class ITunesSearchResponse(
    val resultCount: Int,
    val results: List<ITunesPodcast>
)