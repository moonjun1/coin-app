package com.example.fragmentapp.utils

object CoinNameMapper {
    private val koreanNames = mapOf(
        "bitcoin" to "비트코인",
        "ethereum" to "이더리움",
        "tether" to "테더",
        "ripple" to "리플",
        "binancecoin" to "바이낸스코인",
        "usd-coin" to "USD코인",
        "solana" to "솔라나",
        "tron" to "트론",
        "staked-ether" to "스테이킹 이더리움",
        "dogecoin" to "도지코인",
        "cardano" to "카르다노",
        "chainlink" to "체인링크",
        "stellar" to "스텔라루멘",
        "polkadot" to "폴카닷",
        "uniswap" to "유니스왑",
        "litecoin" to "라이트코인",
        "avalanche-2" to "아발란체",
        "shiba-inu" to "시바이누",
        "dai" to "다이",
        "polygon-ecosystem-token" to "폴리곤",
        "cosmos" to "코스모스",
        "ethereum-classic" to "이더리움클래식",
        "monero" to "모네로",
        "filecoin" to "파일코인",
        "bitcoin-cash" to "비트코인캐시",
        "aptos" to "앱토스",
        "near" to "니어프로토콜",
        "algorand" to "알고랜드",
        "vechain" to "비체인",
        "internet-computer" to "인터넷컴퓨터",
        "hedera-hashgraph" to "헤데라",
        "the-open-network" to "톤코인",
        "kaspa" to "카스파",
        "arbitrum" to "아비트럼",
        "sui" to "수이",
        "optimism" to "옵티미즘",
        "zcash" to "지캐시"
    )

    fun getKoreanName(id: String): String? {
        return koreanNames[id]
    }
}
