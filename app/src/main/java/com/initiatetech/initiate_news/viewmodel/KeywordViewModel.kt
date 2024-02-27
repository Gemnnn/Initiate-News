package com.initiatetech.initiate_news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KeywordViewModel : ViewModel() {
    private val _keywords = MutableLiveData<List<String>>()
    val keywords : LiveData<List<String>> = _keywords

    fun addKeyword(keyword: String) {
        val currentList = _keywords.value ?: emptyList()
        _keywords.value = currentList + keyword

    }

    fun removeKeyword(keyword: String) {
        val currentList = _keywords.value ?: emptyList()
        _keywords.value = currentList.filterNot { it == keyword }
    }
}