package co.edu.udea.compumovil.gr07_20251.lab2.data.model

data class EpisodePlayerState(
    var isPaused: Boolean = false,
    var isPreparing: Boolean = false,
    var progress: Float = 0f,
    var currentTime: Int = 0,
    var duration: Int = 0
)