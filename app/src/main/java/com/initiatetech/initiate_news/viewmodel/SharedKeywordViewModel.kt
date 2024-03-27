package com.initiatetech.initiate_news.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.model.AcceptKeyword
import com.initiatetech.initiate_news.model.ApiResponse
import com.initiatetech.initiate_news.model.Keyword
import com.initiatetech.initiate_news.model.KeywordResponse
import com.initiatetech.initiate_news.model.SharedKeyword
import com.initiatetech.initiate_news.repository.SharedKeywordRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

class SharedKeywordViewModel(private val sharedKeywordRepository: SharedKeywordRepository,
                            context: Context?) : ViewModel() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val context: Context?
        get() = contextRef.get()

    private val _username : String? = getUserEmail()
    private val _sharedKeywords = MutableLiveData<List<SharedKeyword>>()
    val sharedKeywords : LiveData<List<SharedKeyword>> = _sharedKeywords

    fun getSharedKeywords() {
        if (_username != null) {
            _sharedKeywords.value = emptyList()
            sharedKeywordRepository.getSharedKeywords(_username).enqueue(object : Callback<List<SharedKeyword>> {
                override fun onResponse(call: Call<List<SharedKeyword>>, response: Response<List<SharedKeyword>>) {

                    if (response.isSuccessful) {
                        Log.d("ShareKeyword", "getSharedKeywords Successful response")

                        val skResponseList = response.body() // is a List<SharedKeyword>
                        var currentList = _sharedKeywords.value ?: emptyList()
                        skResponseList?.forEach { sharedKeyword ->
                            _sharedKeywords.value = currentList + sharedKeyword
                            currentList = _sharedKeywords.value!!

                            Log.d("SharedKeyword", "${sharedKeyword.keyword} was got and added to list")
                        }
                        Log.d("SharedKeyword", "All keywords: ${_sharedKeywords.value.toString()}")

                    } else {
                        Log.d("SharedKeyword", "getSharedKeywords failed response")

                    }
                }

                override fun onFailure(call: Call<List<SharedKeyword>>, t: Throwable) {
                    Log.e("SharedKeyword", "getSharedKeyword Error", t)
                }
            })
        }
    }

    fun shareKeyword(friendUsername: String, keyword: String) {
        if (_username != null) {
            val sharedKeyword = SharedKeyword(_username, friendUsername, keyword)
            sharedKeywordRepository.shareKeyword(sharedKeyword).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("ShareKeyword", "shareKeyword Successful response")

                        Toast.makeText(context, "Successfully shared ${sharedKeyword.keyword} " +
                                "keyword with ${sharedKeyword.receiver}", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("SharedKeyword", "sharedKeyword failed response")

                        Toast.makeText(context, "Sharing keyword was unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("SharedKeyword", "shareKeyword Error", t)
                }
            })
        }
    }

    fun acceptOrRejectKeyword(keyword: String, accept: Boolean) {
        if (_username != null) {
            val acceptKeyword = AcceptKeyword(_username, keyword, accept)
            sharedKeywordRepository.acceptOrRejectKeyword(acceptKeyword).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("ShareKeyword", "acceptOrRejectKeyword Successful response")

                        if (acceptKeyword.accept) {
                            Toast.makeText(context, "Successfully accepted ${acceptKeyword.keyword}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Successfully rejected ${acceptKeyword.keyword}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d("SharedKeyword", "acceptOrRejectKeyword failed response")

                        Toast.makeText(context, "Accepting/Rejecting was unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("SharedKeyword", "acceptOrRejectKeyword Error", t)
                }
            })
        }
    }

    private fun getUserEmail(): String? {
        val sharedPref = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("user_email", null)
    }

    class SharedKeywordViewModelFactory(private val sharedKeywordRepository: SharedKeywordRepository,
                                        private val context: Context?) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(KeywordViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SharedKeywordViewModel(sharedKeywordRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
