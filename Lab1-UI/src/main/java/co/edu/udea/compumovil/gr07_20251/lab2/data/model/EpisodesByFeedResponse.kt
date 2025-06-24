package co.edu.udea.compumovil.gr07_20251.lab2.data.model

import com.google.gson.annotations.SerializedName

data class EpisodesByFeedResponse(
    @SerializedName("status") val status: String,
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<Episode>
)