package com.initiatetech.initiate_news.model

data class NewsDetailResponse(
    val id: Int,
    val title: String,
    val shortTitle: String,
    val sourceUrl: String,
    val author: String,
    val publishedDate: String,
    val content: String
)
