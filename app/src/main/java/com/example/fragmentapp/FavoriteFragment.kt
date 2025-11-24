package com.example.fragmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        emptyView = view.findViewById(R.id.empty_view)

        recyclerView.layoutManager = LinearLayoutManager(context)

        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            try {
                val favoriteIds = FavoriteManager.getFavorites(requireContext())

                if (favoriteIds.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    val allCryptos = RetrofitClient.api.getCryptoList()
                    val favoriteCryptos = allCryptos.filter { it.id in favoriteIds }

                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE

                    recyclerView.adapter = CryptoAdapter(favoriteCryptos) { cryptoId ->
                        openDetailFragment(cryptoId)
                    }
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
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}
