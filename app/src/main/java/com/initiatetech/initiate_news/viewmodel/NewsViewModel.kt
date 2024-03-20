package com.initiatetech.initiate_news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.model.LocationNewsResponse
import com.initiatetech.initiate_news.model.NewsDetailResponse
import com.initiatetech.initiate_news.model.NewsResponse
import com.initiatetech.initiate_news.repository.NewsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsItems = MutableLiveData<List<NewsResponse>>()
    val newsItems: LiveData<List<NewsResponse>> = _newsItems

    private val _newsDetail = MutableLiveData<NewsDetailResponse?>()
    val newsDetail: LiveData<NewsDetailResponse?> = _newsDetail

    private val _locationNewsItems = MutableLiveData<List<LocationNewsResponse>>()
    val locationNewsItems: LiveData<List<LocationNewsResponse>> = _locationNewsItems

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


    fun fetchNewsDetail(username: String, newsId: Int) {
        newsRepository.getNewsDetail(username, newsId).enqueue(object : Callback<NewsDetailResponse> {
            override fun onResponse(call: Call<NewsDetailResponse>, response: Response<NewsDetailResponse>) {
                if (response.isSuccessful) {
                    _newsDetail.postValue(response.body())
                } else {
                    _newsDetail.postValue(null)
                }
            }

            override fun onFailure(call: Call<NewsDetailResponse>, t: Throwable) {
                _newsDetail.postValue(null)
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

    fun loadLocationBasedNews(username: String, location: String) {
        _isFetchingData.value = true
        newsRepository.getAllLocationNews(username, location).enqueue(object : Callback<List<LocationNewsResponse>> {
            override fun onResponse(call: Call<List<LocationNewsResponse>>, response: Response<List<LocationNewsResponse>>) {
                if (response.isSuccessful) {
                    _locationNewsItems.postValue(response.body() ?: emptyList())
                } else {
                    // Log error or handle it as needed
                    _newsItems.postValue(emptyList())
                }
                _isFetchingData.value = false
            }

            override fun onFailure(call: Call<List<LocationNewsResponse>>, t: Throwable) {
                // Log error or handle it as needed
                _isFetchingData.value = false
            }
        })
    }

}
