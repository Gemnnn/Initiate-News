package com.initiatetech.initiate_news.repository

import com.initiatetech.initiate_news.api.RetrofitClient

class FriendRepository {
    fun getAllFriends(username: String) = RetrofitClient.instance.getAllFriends(username)
    fun requestFriend(username: String, friendUsername: String) = RetrofitClient.instance.requestFriend(username, friendUsername)
    fun acceptFriend(username: String, friendUsername: String) = RetrofitClient.instance.acceptFriend(username, friendUsername)
    fun rejectFriend(username: String, friendUsername: String) = RetrofitClient.instance.rejectFriend(username, friendUsername)
}