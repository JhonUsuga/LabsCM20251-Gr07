package co.edu.udea.compumovil.gr07_20251.lab2.data.model

import com.google.gson.annotations.SerializedName

data class PodcastResponse(
    @SerializedName("status") val status: String,
    @SerializedName("count") val count: Int,
    @SerializedName("feeds") val feeds: List<Feed>
)

data class Feed(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("episodes") val episodes: List<Episode>?
)