package com.initiatetech.initiate_news.api

import com.initiatetech.initiate_news.model.ApiResponse
import com.initiatetech.initiate_news.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("api/user/register")
    fun registerUser(@Body user: User): Call<ApiResponse>

    @POST("api/user/login")
    fun loginUser(@Body loginInfo: User): Call<ApiResponse>
}