package com.example.fragmentapp.fragments

import android.os.Bundle
import com.example.fragmentapp.R
import com.example.fragmentapp.adapters.CryptoAdapter
import com.example.fragmentapp.api.RetrofitClient
import com.example.fragmentapp.models.Crypto
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CryptoCategoryFragment : Fragment() {

    private var category: String? = null
    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): CryptoCategoryFragment {
            val fragment = CryptoCategoryFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString(ARG_CATEGORY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crypto_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadCryptoData()
    }

    private fun loadCryptoData() {
        lifecycleScope.launch {
            try {
                if (category == "nft") {
                    val mockNftList = getMockNftData()
                    recyclerView.adapter = CryptoAdapter(mockNftList) { cryptoId ->
                        Toast.makeText(
                            context,
                            "NFT 상세 정보는 개발중입니다 🚧",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@launch
                }

                val allCryptos = RetrofitClient.api.getCryptoList()

                val filteredList = when (category) {
                    "major" -> allCryptos.take(10)
                    "alt" -> allCryptos.drop(10).take(90)
                    else -> allCryptos
                }

                recyclerView.adapter = CryptoAdapter(filteredList) { cryptoId ->
                    openDetailFragment(cryptoId)
                }

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "데이터를 불러오는데 실패했습니다: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getMockNftData(): List<Crypto> {
        return listOf(
            Crypto(
                id = "apecoin",
                symbol = "ape",
                name = "ApeCoin",
                image = "https://assets.coingecko.com/coins/images/24383/large/apecoin.jpg",
                current_price = 5420.0,
                price_change_percentage_24h = 2.5,
                market_cap = 1234567890,
                total_volume = 123456789
            ),
            Crypto(
                id = "decentraland",
                symbol = "mana",
                name = "Decentraland",
                image = "https://assets.coingecko.com/coins/images/878/large/decentraland-mana.png",
                current_price = 680.0,
                price_change_percentage_24h = -1.2,
                market_cap = 987654321,
                total_volume = 98765432
            ),
            Crypto(
                id = "the-sandbox",
                symbol = "sand",
                name = "The Sandbox",
                image = "https://assets.coingecko.com/coins/images/12129/large/sandbox_logo.jpg",
                current_price = 540.0,
                price_change_percentage_24h = 3.8,
                market_cap = 876543210,
                total_volume = 87654321
            ),
            Crypto(
                id = "axie-infinity",
                symbol = "axs",
                name = "Axie Infinity",
                image = "https://assets.coingecko.com/coins/images/13029/large/axie_infinity_logo.png",
                current_price = 9800.0,
                price_change_percentage_24h = -2.3,
                market_cap = 765432109,
                total_volume = 76543210
            ),
            Crypto(
                id = "enjincoin",
                symbol = "enj",
                name = "Enjin Coin",
                image = "https://assets.coingecko.com/coins/images/1102/large/enjin-coin-logo.png",
                current_price = 320.0,
                price_change_percentage_24h = 1.5,
                market_cap = 654321098,
                total_volume = 65432109
            ),
            Crypto(
                id = "gala",
                symbol = "gala",
                name = "Gala",
                image = "https://assets.coingecko.com/coins/images/12493/large/GALA-COINGECKO.png",
                current_price = 45.0,
                price_change_percentage_24h = 4.2,
                market_cap = 543210987,
                total_volume = 54321098
            ),
            Crypto(
                id = "flow",
                symbol = "flow",
                name = "Flow",
                image = "https://assets.coingecko.com/coins/images/13446/large/flow.png",
                current_price = 1200.0,
                price_change_percentage_24h = -0.8,
                market_cap = 432109876,
                total_volume = 43210987
            ),
            Crypto(
                id = "immutable-x",
                symbol = "imx",
                name = "Immutable X",
                image = "https://assets.coingecko.com/coins/images/17233/large/immutableX-symbol-BLK-RGB.png",
                current_price = 2300.0,
                price_change_percentage_24h = 5.1,
                market_cap = 321098765,
                total_volume = 32109876
            ),
            Crypto(
                id = "theta-token",
                symbol = "theta",
                name = "Theta Network",
                image = "https://assets.coingecko.com/coins/images/2538/large/theta-token-logo.png",
                current_price = 1800.0,
                price_change_percentage_24h = -1.9,
                market_cap = 210987654,
                total_volume = 21098765
            ),
            Crypto(
                id = "chiliz",
                symbol = "chz",
                name = "Chiliz",
                image = "https://assets.coingecko.com/coins/images/8834/large/Chiliz.png",
                current_price = 150.0,
                price_change_percentage_24h = 2.7,
                market_cap = 109876543,
                total_volume = 10987654
            )
        )
    }

    private fun openDetailFragment(cryptoId: String) {
        val detailFragment = CryptoDetailFragment.newInstance(cryptoId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}
