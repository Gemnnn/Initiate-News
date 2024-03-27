package com.initiatetech.initiate_news.repository

import com.initiatetech.initiate_news.api.RetrofitClient
import com.initiatetech.initiate_news.model.FriendUsername

class FriendRepository {
    fun getAllFriends(username: String) = RetrofitClient.instance.getAllFriends(username)
    fun requestFriend(username: String, friendUsername: FriendUsername) = RetrofitClient.instance.requestFriend(username, friendUsername)
    fun acceptFriend(username: String, friendUsername: FriendUsername) = RetrofitClient.instance.acceptFriend(username, friendUsername)
    fun rejectFriend(username: String, friendUsername: FriendUsername) = RetrofitClient.instance.rejectFriend(username, friendUsername)
}