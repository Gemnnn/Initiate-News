package com.initiatetech.initiate_news.repository

import com.initiatetech.initiate_news.api.RetrofitClient
import com.initiatetech.initiate_news.model.Keyword

class KeywordRepository {
    fun getAllKeywords(username: String) = RetrofitClient.instance.getAllKeywords(username)
    fun addKeyword(keyword: Keyword) = RetrofitClient.instance.addKeyword(keyword)
    fun deleteKeyword(username: String, keyword: String) = RetrofitClient.instance.deleteKeyword(username, keyword)
}