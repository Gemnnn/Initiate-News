package com.initiatetech.initiate_news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.model.NewsResponse
import com.initiatetech.initiate_news.repository.NewsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsItems = MutableLiveData<List<NewsResponse>>()
    val newsItems: LiveData<List<NewsResponse>> = _newsItems

    private val _isFetchingData = MutableLiveData<Boolean>()
    fun fetchNewsForKeyword(username: String, keyword: String) {
        _isFetchingData.postValue(true)
        newsRepository.getAllKeywordNews(username, keyword).enqueue(object : Callback<List<NewsResponse>> {
            override fun onResponse(call: Call<List<NewsResponse>>, response: Response<List<NewsResponse>>) {
                if (response.isSuccessful) {
                    _newsItems.postValue(response.body() ?: emptyList())
                } else {
                    // Handle error scenario
                }
                _isFetchingData.postValue(false)
            }

            override fun onFailure(call: Call<List<NewsResponse>>, t: Throwable) {
                // Handle failure scenario
                _isFetchingData.postValue(false)
            }
        })
    }

    class NewsViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(newsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
