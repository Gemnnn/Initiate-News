package com.initiatetech.initiate_news.repository

import com.initiatetech.initiate_news.api.RetrofitClient
import com.initiatetech.initiate_news.model.PreferenceData

class PreferenceRepository {
    fun getPreferences(email: String?) = RetrofitClient.instance.getPreferences(email)
    fun setPreferences(preferences: PreferenceData) = RetrofitClient.instance.setPreferences(preferences)
}