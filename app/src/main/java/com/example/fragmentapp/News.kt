package com.example.fragmentapp

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("Data")
    val data: List<News>
)

data class News(
    val id: String,
    val title: String,
    val body: String,
    val url: String,
    @SerializedName("source_info")
    val sourceInfo: SourceInfo,
    @SerializedName("imageurl")
    val imageUrl: String?,
    @SerializedName("published_on")
    val publishedOn: Long,
    val categories: String?
)

data class SourceInfo(
    val name: String,
    val img: String?
)
