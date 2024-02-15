package com.initiatetech.initiate_news.model

data class PreferenceData(
    val language: String?,
    val province: String?,
    val country: String?,
    val newsGenerationTime: String?,
    val email: String?,
    val isSetPreference: Boolean?
)

