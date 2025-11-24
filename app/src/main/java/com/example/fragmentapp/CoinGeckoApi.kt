package com.example.fragmentapp

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * CoinGecko API 인터페이스
 *
 * 학습 포인트:
 * 1. Retrofit을 사용한 REST API 호출
 * 2. Coroutine의 suspend 함수 사용
 * 3. Query 파라미터를 통한 데이터 필터링
 */
interface CoinGeckoApi {

    /**
     * 암호화폐 시세 목록 조회
     *
     * @param currency 통화 단위 (krw, usd 등)
     * @param order 정렬 기준 (market_cap_desc: 시가총액 내림차순)
     * @param perPage 페이지당 항목 수
     * @param page 페이지 번호
     * @return 암호화폐 목록
     */
    @GET("coins/markets")
    suspend fun getCryptoList(
        @Query("vs_currency") currency: String = "krw",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): List<Crypto>
}
