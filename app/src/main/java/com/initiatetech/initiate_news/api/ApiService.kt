package com.initiatetech.initiate_news.api

import com.initiatetech.initiate_news.model.ApiResponse
import com.initiatetech.initiate_news.model.PreferenceData
import com.initiatetech.initiate_news.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @POST("api/user/register")
    fun registerUser(@Body user: User): Call<ApiResponse>

    @POST("api/user/login")
    fun loginUser(@Body loginInfo: User): Call<ApiResponse>

    @POST("api/user/logout")
    fun logoutUser(): Call<ApiResponse>

    @GET("api/preference/{email}")
    fun getPreferences(@Path("email") email: String?): Call<ApiResponse>

    @PUT("api/preference")
    fun setPreferences(@Body preferences: PreferenceData): Call<ApiResponse>
}