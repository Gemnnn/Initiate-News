package com.initiatetech.initiate_news.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.model.ApiResponse
import com.initiatetech.initiate_news.model.FriendResponse
import com.initiatetech.initiate_news.model.FriendUsername
import com.initiatetech.initiate_news.repository.FriendRepository
import com.initiatetech.initiate_news.repository.SharedKeywordRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

// TODO: Fix this
//class FriendViewModel(private val friendRepository: FriendRepository,
//                      private val sharedKeywordViewModel: SharedKeywordViewModel,
//                      context: Context?) : ViewModel() {

class FriendViewModel(private val friendRepository: FriendRepository,
                      context: Context?) : ViewModel() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val context: Context?
        get() = contextRef.get()

    private val _username : String? = getUserEmail()

    private val _friends = MutableLiveData<List<FriendResponse>>()
    val friends : LiveData<List<FriendResponse>> = _friends

    fun getAllFriends() {
        if (_username != null) {
            _friends.value = emptyList()
            friendRepository.getAllFriends(_username).enqueue(object : Callback<List<FriendResponse>> {
                override fun onResponse(call: Call<List<FriendResponse>>, response: Response<List<FriendResponse>>) {

                    if (response.isSuccessful) {
                        Log.d("Friend", "getAllFriends Successful response")

                        val friendResponseList = response.body() // is a List<FriendResponse>
                        var currentList = _friends.value ?: emptyList()
                        friendResponseList?.forEach { friendResponse ->
                            _friends.value = currentList + friendResponse
                            currentList = _friends.value!!

                            Log.d("Friend", "${friendResponse.friendUsername} was got and added to list")
                        }
                        Log.d("Friend", "All friends: ${_friends.value.toString()}")

                    } else {
                        Log.d("Friend", "getAllFriends failed response")

                    }
                }

                override fun onFailure(call: Call<List<FriendResponse>>, t: Throwable) {
                    Log.e("Friend", "getAllFriends Error", t)
                }
            })
        }
    }

    fun addFriend(friend: String) {
        // Send friend request
        if (_username != null) {
            val friendUsername = FriendUsername(friend)
            friendRepository.requestFriend(_username, friendUsername).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("Friend", "addFriend (requestFriend) successful response")

                        // Reload the friend list
                        getAllFriends()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.d("Friend", "addFriend (requestFriend) failed response")
                        Toast.makeText(context, "$friendUsername was not sent your request: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("Friend", "addFriend (requestFriend) error", t)
                    Toast.makeText(context, "Adding friend failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun acceptFriend(friend: String) {
        if (_username != null) {
            val friendUsername = FriendUsername(friend)
            friendRepository.acceptFriend(_username, friendUsername).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("Friend", "acceptFriend successful response")

                        // Reload the friend list
                        getAllFriends()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.d("Friend", "acceptFriend failed response")
                        Toast.makeText(context, "Your friend request from $friendUsername was not accepted: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("Friend", "acceptFriend error", t)
                    Toast.makeText(context, "Accepting friend request failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun rejectFriend(friend: String) {
        if (_username != null) {
            val friendUsername = FriendUsername(friend)
            friendRepository.rejectFriend(_username, friendUsername).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("Friend", "rejectFriend successful response")

                        // Reload the friend list
                        getAllFriends()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.d("Friend", "rejectFriend failed response: $errorMessage")
                        Toast.makeText(context, "Your friend request from $friendUsername was not rejected $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("Friend", "rejectFriend error", t)
                    Toast.makeText(context, "Rejecting friend request failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun share() {
        // TODO: Add code for sharing a keyword with a friend

        // Open a dialog to select a keyword to share with send/cancel buttons

        // Open dialog
        // Fill dialog with keywords via getAllKeywords()
        // Click submit/share button that calls shareKeyword()
    }

    private fun getUserEmail(): String? {
        val sharedPref = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("user_email", null)
    }


    // TODO: Fix this
//    class FriendViewModelFactory(private val friendRepository: FriendRepository,
//                                 private val sharedKeywordViewModel: SharedKeywordViewModel,
//                                 private val context: Context?) :
    class FriendViewModelFactory(private val friendRepository: FriendRepository,
                                 private val context: Context?) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                // TODO: Fix this
//                return FriendViewModel(friendRepository, sharedKeywordViewModel, context) as T
                return FriendViewModel(friendRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}