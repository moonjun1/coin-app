package com.example.fragmentapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit 클라이언트 싱글톤
 *
 * 학습 포인트:
 * 1. object 키워드를 통한 싱글톤 패턴
 * 2. lazy 초기화로 필요할 때만 인스턴스 생성
 * 3. OkHttp 로깅 인터셉터로 네트워크 디버깅
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.coingecko.com/api/v3/"

    /**
     * OkHttp 클라이언트 설정
     * - 로깅 인터셉터: API 요청/응답 로그 출력
     * - 타임아웃 설정: 네트워크 지연 대응
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Retrofit 인스턴스
     * lazy: 최초 호출 시에만 초기화되고 이후에는 재사용
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * CoinGecko API 인터페이스 인스턴스
     */
    val api: CoinGeckoApi by lazy {
        retrofit.create(CoinGeckoApi::class.java)
    }
}
