package com.example.fragmentapp

/**
 * 암호화폐 데이터 클래스
 * CoinGecko API 응답 데이터 구조
 */
data class Crypto(
    val id: String,                              // 코인 ID (bitcoin, ethereum 등)
    val symbol: String,                          // 심볼 (BTC, ETH 등)
    val name: String,                            // 이름 (Bitcoin, Ethereum 등)
    val image: String,                           // 로고 이미지 URL
    val current_price: Double,                   // 현재 가격
    val price_change_percentage_24h: Double,     // 24시간 변동률
    val market_cap: Long,                        // 시가총액
    val total_volume: Long                       // 거래량
)
