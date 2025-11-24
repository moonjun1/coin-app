package com.example.fragmentapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsAdapter(
    private val newsList: List<News>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.news_image)
        val titleText: TextView = view.findViewById(R.id.news_title)
        val sourceText: TextView = view.findViewById(R.id.news_source)
        val dateText: TextView = view.findViewById(R.id.news_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]

        holder.titleText.text = news.title
        holder.sourceText.text = news.sourceInfo.name
        holder.dateText.text = formatDate(news.publishedOn)

        holder.itemView.setOnClickListener {
            onItemClick(news.url)
        }
    }

    override fun getItemCount() = newsList.size

    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
        return format.format(date)
    }
}
