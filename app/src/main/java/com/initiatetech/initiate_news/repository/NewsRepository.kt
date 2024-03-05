package com.initiatetech.initiate_news.repository

import com.initiatetech.initiate_news.api.RetrofitClient

class NewsRepository {
    fun getAllKeywordNews(username: String, keyword: String) = RetrofitClient.instance.getAllKeywordNews(username, keyword)
    fun getNewsDetail(username: String, newsId: Int) = RetrofitClient.instance.getKeywordNews(username, newsId)

}
