package com.initiatetech.initiate_news.repository

import com.initiatetech.initiate_news.api.RetrofitClient
import com.initiatetech.initiate_news.model.User

class UserRepository {
    fun registerUser(user: User) = RetrofitClient.instance.registerUser(user)
    fun loginUser(user: User) = RetrofitClient.instance.loginUser(user)
    fun logoutUser() = RetrofitClient.instance.logoutUser()

}
