package co.edu.udea.compumovil.gr07_20251.lab2.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://itunes.apple.com/"

    val retrofitService: PodcastApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PodcastApiService::class.java)
    }
}