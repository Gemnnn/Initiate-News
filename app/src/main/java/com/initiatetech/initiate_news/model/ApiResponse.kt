package com.initiatetech.initiate_news.model

data class ApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val preferenceData: PreferenceData? = null
)

data class PreferenceResponse(
    val language: String?,
    val province: String?,
    val country: String?,
    val newsGenerationTime: String?,
    val email: String?,
    val isSetPreference: Boolean?
)
