package com.initiatetech.initiate_news.model

data class FriendResponse (
    val friendUsername: String,
    val status: Int,
    val isRequestedByCurrentUser: Boolean
)