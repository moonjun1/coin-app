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
 * 대시보드 Fragment
 * 주요 암호화폐 TOP 5 요약 정보 표시
 */
class DashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadTopCryptos()
    }

    private fun loadTopCryptos() {
        lifecycleScope.launch {
            try {
                val allCryptos = RetrofitClient.api.getCryptoList()
                val topFive = allCryptos.take(5)

                recyclerView.adapter = CryptoAdapter(topFive)

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "데이터를 불러오는데 실패했습니다: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
