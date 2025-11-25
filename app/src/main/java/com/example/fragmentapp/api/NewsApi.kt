package com.example.fragmentapp.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.fragmentapp.models.NewsResponse

interface NewsApi {
    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") query: String = "cryptocurrency OR bitcoin OR ethereum",
        @Query("language") language: String = "ko",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("pageSize") pageSize: Int = 50
    ): NewsResponse
}
