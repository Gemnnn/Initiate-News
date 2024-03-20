package com.initiatetech.initiate_news.model

data class SharedKeyword(
    val requester: String,
    val receiver: String,
    val keyword: String
)