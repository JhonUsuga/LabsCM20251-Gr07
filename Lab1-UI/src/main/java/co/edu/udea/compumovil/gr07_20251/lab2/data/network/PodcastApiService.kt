package co.edu.udea.compumovil.gr07_20251.lab2.data.network

import co.edu.udea.compumovil.gr07_20251.lab2.data.model.ITunesSearchResponse
import co.edu.udea.compumovil.gr07_20251.lab2.data.model.EpisodesByFeedResponse
import co.edu.udea.compumovil.gr07_20251.lab2.data.model.PodcastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PodcastApiService {
    @GET("search")
    suspend fun searchITunesPodcasts(
        @Query("media") media: String = "podcast",
        @Query("term") term: String = "technology"
    ): Response<ITunesSearchResponse>

    @GET("api/1.0/episodes/byfeedid")
    suspend fun getEpisodesByFeedId(
        @Query("id") feedId: Int
    ): Response<EpisodesByFeedResponse>
}