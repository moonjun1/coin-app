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

class NewsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.news_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadNews()
    }

    private fun loadNews() {
        emptyView.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val newsResponse = RetrofitClient.newsApi.getNews()
                val newsList = newsResponse.data

                if (newsList.isEmpty()) {
                    emptyView.text = "뉴스가 없습니다"
                } else {
                    emptyView.visibility = View.GONE
                    recyclerView.adapter = NewsAdapter(newsList) { newsUrl ->
                        openNewsDetail(newsUrl)
                    }
                }

            } catch (e: Exception) {
                emptyView.text = "뉴스를 불러오는데 실패했습니다"
                Toast.makeText(
                    context,
                    "뉴스를 불러오는데 실패했습니다: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openNewsDetail(newsUrl: String) {
        val detailFragment = NewsDetailFragment.newInstance(newsUrl)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}
