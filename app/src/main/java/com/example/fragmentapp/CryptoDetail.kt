package com.example.fragmentapp

data class CryptoDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val image: CryptoImage,
    val market_cap_rank: Int?,
    val market_data: MarketData,
    val description: Description?
)

data class CryptoImage(
    val large: String
)

data class MarketData(
    val current_price: Map<String, Double>,
    val price_change_percentage_24h: Double?,
    val price_change_percentage_7d: Double?,
    val price_change_percentage_30d: Double?,
    val market_cap: Map<String, Long>,
    val total_volume: Map<String, Long>,
    val high_24h: Map<String, Double>,
    val low_24h: Map<String, Double>,
    val ath: Map<String, Double>,
    val atl: Map<String, Double>
)

data class Description(
    val ko: String?,
    val en: String?
)
