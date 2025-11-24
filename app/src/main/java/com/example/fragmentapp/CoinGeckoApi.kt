package com.example.fragmentapp

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("coins/markets")
    suspend fun getCryptoList(
        @Query("vs_currency") currency: String = "krw",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): List<Crypto>

    @GET("coins/{id}")
    suspend fun getCryptoDetail(
        @Path("id") id: String,
        @Query("localization") localization: Boolean = true,
        @Query("tickers") tickers: Boolean = false,
        @Query("market_data") marketData: Boolean = true,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false
    ): CryptoDetail

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") currency: String = "krw",
        @Query("days") days: Int = 7
    ): MarketChart
}
