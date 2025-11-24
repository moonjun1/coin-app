package com.example.fragmentapp

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("data/v2/news/")
    suspend fun getNews(
        @Query("lang") language: String = "EN",
        @Query("sortOrder") sortOrder: String = "latest"
    ): NewsResponse
}
