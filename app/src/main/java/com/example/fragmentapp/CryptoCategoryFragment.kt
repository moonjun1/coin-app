package com.example.fragmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

/**
 * 각 카테고리별 암호화폐 목록을 보여주는 Fragment
 *
 * 학습 포인트:
 * 1. Fragment 인자 전달 (Bundle 사용)
 * 2. companion object를 통한 Factory 패턴
 * 3. RecyclerView를 Fragment에서 사용
 * 4. Coroutine을 통한 API 비동기 호출
 */
class CryptoCategoryFragment : Fragment() {

    private var category: String? = null
    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val ARG_CATEGORY = "category"

        /**
         * Fragment 생성 팩토리 메서드
         * Fragment에 인자를 전달할 때는 Bundle을 사용!
         */
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
        // arguments에서 전달받은 데이터 추출
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

        // API에서 데이터 가져오기
        loadCryptoData()
    }

    /**
     * API에서 암호화폐 데이터를 가져와 RecyclerView에 표시
     */
    private fun loadCryptoData() {
        lifecycleScope.launch {
            try {
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

    private fun openDetailFragment(cryptoId: String) {
        val detailFragment = CryptoDetailFragment.newInstance(cryptoId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}
