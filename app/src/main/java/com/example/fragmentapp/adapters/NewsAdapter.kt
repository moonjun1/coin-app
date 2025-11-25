package com.example.fragmentapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.fragmentapp.R
import com.example.fragmentapp.models.News

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
        holder.sourceText.text = news.source.name
        holder.dateText.text = formatDate(news.publishedAt)

        holder.itemView.setOnClickListener {
            onItemClick(news.url)
        }
    }

    override fun getItemCount() = newsList.size

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            dateString
        }
    }
}
