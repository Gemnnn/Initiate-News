package com.initiatetech.initiate_news.model

import com.google.gson.annotations.SerializedName

data class LocationNewsResponse(
    val id: Int,
    val title: String,
    val shortTitle: String,
    @SerializedName("publishedDate") val publishedDate: String,
    var keyword: String
)
