package co.edu.udea.compumovil.gr07_20251.lab2.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://itunes.apple.com/"
    private const val API_KEY = "ECMYS5PNMVDKWJH2A9CG"
    private const val API_SECRET = "NEpZ4XGQXGCAMNmHrz2zV#hGSCuNegPg7snXZPbx"

    val retrofitService: PodcastApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .addHeader("X-Auth-Key", "ECMYS5PNMVDKWJH2A9CG")
                    .addHeader("X-Auth-Secret", "NEpZ4XGQXGCAMNmHrz2zV#hGSCuNegPg7snXZPbx")
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