package co.edu.udea.compumovil.gr07_20251.lab2.data.model

import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("enclosureUrl") val enclosureUrl: String,
    @SerializedName("datePublished") val datePublished: Long
)