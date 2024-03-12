package com.initiatetech.initiate_news.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.model.ApiResponse
import com.initiatetech.initiate_news.model.Keyword
import com.initiatetech.initiate_news.model.KeywordResponse
import com.initiatetech.initiate_news.model.NewsResponse
import com.initiatetech.initiate_news.repository.KeywordRepository
import com.initiatetech.initiate_news.repository.NewsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

class KeywordViewModel(private val keywordRepository: KeywordRepository,
                       private val newsRepository: NewsRepository,
                       context: Context?) : ViewModel() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val context: Context?
        get() = contextRef.get()

    private val _username : String? = getUserEmail()
    private val _keywords = MutableLiveData<List<String>>()
    val keywords : LiveData<List<String>> = _keywords

    fun getKeywords() {
        if (_username != null) {
            _keywords.value = emptyList()
            keywordRepository.getAllKeywords(_username).enqueue(object : Callback<List<KeywordResponse>> {
                override fun onResponse(call: Call<List<KeywordResponse>>, response: Response<List<KeywordResponse>>) {

                    if (response.isSuccessful) {
                        Log.d("Keyword", "getKeywords Successful response")

                        val keywordResponseList = response.body() // is a List<KeywordResponse>
                        var currentList = _keywords.value ?: emptyList()
                        keywordResponseList?.forEach { keywordResponse ->
                            _keywords.value = currentList + keywordResponse.keyword
                            currentList = _keywords.value!!

                            Log.d("Keyword", "${keywordResponse.keyword} was got and added to list")
                        }
                        Log.d("Keyword", "All keywords: ${_keywords.value.toString()}")

                    } else {
                        Log.d("Keyword", "getKeywords failed response")

                    }
                }

                override fun onFailure(call: Call<List<KeywordResponse>>, t: Throwable) {
                    Log.e("Keyword", "getKeyword Error", t)
                }
            })
        }
    }

    fun addKeyword(keyword: String) {
        if (_username != null) {
            val newKeyword = Keyword(_username, keyword)
            Log.d("Keyword", "$newKeyword $_username $keyword")
            keywordRepository.addKeyword(newKeyword).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("Keyword", "addKeyword successful response")
                        Toast.makeText(context, "$keyword was added to your Home", Toast.LENGTH_SHORT).show()

                        // Check to see if news for keyword already exists or not
                        keywordHasNoNews(newKeyword) { hasNoNews ->
                            if (hasNoNews) {
                                Log.d("Keyword", "keyword has no news, generating news now")

                                // If true then the new Keyword has no news, add function to generate news here
                                getFirstKeywordNews(_username, keyword)
                            }
                        }

                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        val errorCode = response.code()
                        Log.d("Keyword", "addKeyword failed response code: $errorCode $errorMessage")
                        Toast.makeText(context, "The keyword was not added: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("Keyword", "addKeyword error", t)
                    Toast.makeText(context, "Adding keyword failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun removeKeyword(keyword: String) {
        if (_username != null) {
            keywordRepository.deleteKeyword(_username, keyword).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("Keyword", "removeKeyword successful response")
                        Toast.makeText(context, "$keyword keyword was deleted", Toast.LENGTH_SHORT).show()

                        // Reload the keyword list
                        getKeywords()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.d("Keyword", "removeKeyword failed response")
                        Toast.makeText(context, "$keyword keyword was not deleted: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("Keyword", "removeKeyword error", t)
                    Toast.makeText(context, "Deleting keyword failed", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun keywordHasNoNews(newKeyword: Keyword, callback: (Boolean) -> Unit) {
        val username = newKeyword.username
        val keyword = newKeyword.word

        newsRepository.getAllKeywordNews(username, keyword).enqueue(object : Callback<List<NewsResponse>> {
            override fun onResponse(call: Call<List<NewsResponse>>, response: Response<List<NewsResponse>>) {
                if (response.isSuccessful) {
                    Log.d("Keyword", "getAllKeywordNews successful response")
                    // If there is news then false, if there is no news then true
                    val noNews = response.body()?.isEmpty() ?: false
                    Log.d("Keyword", "noNews is $noNews")
                    callback(noNews)
                } else {
                    // Handle unsuccessful response
                    Log.d("Keyword", "getAllKeywordNews failed response")
                    callback(false) // Assuming news for unsuccessful responses
                }
            }

            override fun onFailure(call: Call<List<NewsResponse>>, t: Throwable) {
                Log.e("Keyword", "getAllKeywordNews error", t)
                // Handle failure
                callback(false) // Assuming news for failures
            }
        })
    }

    private fun getFirstKeywordNews(username: String, keyword: String) {
        newsRepository.getFirstKeywordNews(username, keyword).enqueue(object : Callback<List<NewsResponse>> {
            override fun onResponse(call: Call<List<NewsResponse>>, response: Response<List<NewsResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    Log.d("News", "getFirstKeywordNews successful response: initial news loaded")
                } else {
                    Log.d("News", "getFirstKeywordNews failed response: initial news not loaded correctly")
                    Toast.makeText(context, "$keyword news did not load correctly", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<NewsResponse>>, t: Throwable) {
                Log.e("News", "getFirstKeywordNews error", t)
                Toast.makeText(context, "Loading $keyword news failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getUserEmail(): String? {
        val sharedPref = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("user_email", null)
    }

    class KeywordViewModelFactory(private val keywordRepository: KeywordRepository,
                                  private val newsRepository: NewsRepository,
                                  private val context: Context?) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(KeywordViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return KeywordViewModel(keywordRepository, newsRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
