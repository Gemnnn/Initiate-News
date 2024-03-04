package com.initiatetech.initiate_news.api

import com.initiatetech.initiate_news.model.ApiResponse
import com.initiatetech.initiate_news.model.Keyword
import com.initiatetech.initiate_news.model.KeywordResponse
import com.initiatetech.initiate_news.model.NewsResponse
import com.initiatetech.initiate_news.model.PreferenceData
import com.initiatetech.initiate_news.model.PreferenceResponse
import com.initiatetech.initiate_news.model.SumNewsResponse
import com.initiatetech.initiate_news.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {

    // User calls
    @POST("api/user/register")
    fun registerUser(@Body user: User): Call<ApiResponse>

    @POST("api/user/login")
    fun loginUser(@Body loginInfo: User): Call<ApiResponse>

    @POST("api/user/logout")
    fun logoutUser(): Call<ApiResponse>


    // Preferences calls
    @GET("api/preference/{email}")
    fun getPreferences(@Path("email") email: String?): Call<PreferenceResponse>

    @PUT("api/preference")
    fun setPreferences(@Body preferences: PreferenceData): Call<ApiResponse>

    // Keyword calls
    @GET("api/keyword/{username}")
    fun getAllKeywords(@Path("username") username: String): Call<List<KeywordResponse>>

    @POST("api/keyword")
    fun addKeyword(@Body keyword: Keyword): Call<ApiResponse>

    @DELETE("api/keyword/{username}/{keyword}")
    fun deleteKeyword(@Path("username") username: String, @Path("keyword") keyword: String): Call<ApiResponse>


    // Summarized News calls
    @GET("api/News/keyword/{username}/{keyword}")
    fun getAllKeywordNews(@Path("username") username: String, @Path("keyword") keyword: String): Call<List<NewsResponse>>

    @GET("api/news/location/{username}/{keyword}")
    fun getLocationNewsByKeyword(@Path("username") username: String, @Path("keyword") keyword: String): Call<SumNewsResponse>

    @GET("api/news/keyword/{username}/{keyword}")
    fun getNewsByFirstKeyword(@Path("username") username: String, @Path("keyword") keyword: String): Call<SumNewsResponse>

    @GET("api/news/keyword/{username}/{keyword}")
    fun getNewsByFirstLocation(@Path("username") username: String, @Path("keyword") keyword: String): Call<SumNewsResponse>

    // News calls
    @GET("api/news/keyword/{username}/{id}")
    fun getFullLocationNews(@Path("username") username: String, @Path("id") id: Int): Call<NewsResponse>

    @GET("api/news/keyword/{username}/{id}")
    fun getFullKeywordNews(@Path("username") username: String, @Path("id") id: Int): Call<NewsResponse>

}