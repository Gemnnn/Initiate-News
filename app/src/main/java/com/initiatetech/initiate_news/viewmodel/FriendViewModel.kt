package com.initiatetech.initiate_news.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.repository.FriendRepository
import java.lang.ref.WeakReference

class FriendViewModel(private val friendRepository: FriendRepository,
                      context: Context?) : ViewModel() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val context: Context?
        get() = contextRef.get()

    private val _username : String? = getUserEmail()

    // TODO: GIVE THIS CORRECT RESPONSE VALUES
    private val _friends = MutableLiveData<List<String>>()
    val friends : LiveData<List<String>> = _friends

    fun getAllFriends() {
        // TODO: Add code for API Call to get all friends

    }

    fun messageFriend() {
        // TODO: Add code for sending a message to selected friend

    }

    fun removeFriend() {
        // TODO: Add code for API Call to remove a friend from friends list

    }

    private fun getUserEmail(): String? {
        val sharedPref = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("user_email", null)
    }

    class FriendViewModelFactory(private val friendRepository: FriendRepository,
                                 private val context: Context?) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FriendViewModel(friendRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}