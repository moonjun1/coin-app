package com.example.fragmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 각 카테고리별 뉴스 목록을 보여주는 Fragment
 *
 * 학습 포인트:
 * 1. Fragment 인자 전달 (Bundle 사용)
 * 2. companion object를 통한 Factory 패턴
 * 3. RecyclerView를 Fragment에서 사용
 */
class NewsCategoryFragment : Fragment() {

    private var category: String? = null

    companion object {
        private const val ARG_CATEGORY = "category"

        /**
         * Fragment 생성 팩토리 메서드
         * Fragment에 인자를 전달할 때는 Bundle을 사용!
         */
        fun newInstance(category: String): NewsCategoryFragment {
            val fragment = NewsCategoryFragment()
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
        return inflater.inflate(R.layout.fragment_news_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 더미 데이터 생성
        val newsList = generateDummyNews(category ?: "일반")
        recyclerView.adapter = NewsAdapter(newsList)
    }

    private fun generateDummyNews(category: String): List<NewsItem> {
        return listOf(
            NewsItem("$category 뉴스 1", "${category}에 대한 최신 소식입니다."),
            NewsItem("$category 뉴스 2", "${category} 관련 중요한 발표가 있었습니다."),
            NewsItem("$category 뉴스 3", "${category} 분야의 새로운 동향입니다."),
            NewsItem("$category 뉴스 4", "${category}에서 주목할 만한 일이 발생했습니다."),
            NewsItem("$category 뉴스 5", "${category} 전문가들의 의견을 들어봅니다.")
        )
    }
}

/**
 * 뉴스 데이터 클래스
 */
data class NewsItem(
    val title: String,
    val summary: String
)

/**
 * RecyclerView 어댑터
 */
class NewsAdapter(private val newsList: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.news_title)
        val summaryText: TextView = view.findViewById(R.id.news_summary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.titleText.text = news.title
        holder.summaryText.text = news.summary
    }

    override fun getItemCount() = newsList.size
}