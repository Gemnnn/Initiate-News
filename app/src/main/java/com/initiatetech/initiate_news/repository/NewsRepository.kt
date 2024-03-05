package com.initiatetech.initiate_news.repository

import com.initiatetech.initiate_news.api.RetrofitClient

class NewsRepository {
    // Summarized news
    fun getAllKeywordNews(username: String, keyword: String) = RetrofitClient.instance.getAllKeywordNews(username, keyword)
    fun getFirstKeywordNews(username: String, keyword: String) = RetrofitClient.instance.getFirstKeywordNews(username, keyword)

    // News
    fun getNewsDetail(username: String, newsId: Int) = RetrofitClient.instance.getKeywordNews(username, newsId)
}
