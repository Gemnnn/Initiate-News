package com.initiatetech.initiate_news.repository

import com.initiatetech.initiate_news.api.RetrofitClient
import com.initiatetech.initiate_news.model.AcceptKeyword
import com.initiatetech.initiate_news.model.SharedKeyword

class SharedKeywordRepository {
    fun getSharedKeywords(username: String) = RetrofitClient.instance.getSharedKeywords(username)
    fun shareKeyword(sharedKeyword: SharedKeyword) = RetrofitClient.instance.shareKeyword(sharedKeyword)
    fun acceptOrRejectKeyword(acceptKeyword: AcceptKeyword) = RetrofitClient.instance.acceptOrRejectKeyword(acceptKeyword)
}