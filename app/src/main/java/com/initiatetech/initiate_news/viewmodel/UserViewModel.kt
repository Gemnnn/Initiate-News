package com.initiatetech.initiate_news.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.model.ApiResponse
import com.initiatetech.initiate_news.model.User
import com.initiatetech.initiate_news.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    // LiveData for login status
    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> = _loginStatus

    // LiveData for registration status
    private val _registrationStatus = MutableLiveData<RegistrationStatus>()
    val registrationStatus: LiveData<RegistrationStatus> = _registrationStatus

    fun loginUser(email: String, password: String) {
        val loginInfo = User(email, password) // Assuming UserLogin is similar or use User directly if same attributes
        Log.d("Login", "Start")
        userRepository.loginUser(loginInfo).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Log.d("Login", "success")
                    _loginStatus.value = LoginStatus.SUCCESS
                } else {
                    Log.d("Login", "fail")
                    _loginStatus.value = LoginStatus.FAILURE
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _loginStatus.value = LoginStatus.ERROR
            }
        })
    }

    fun registerUser(email: String, password: String) {
        val newUser = User(email, password) // Corrected to create a new User instance

        userRepository.registerUser(newUser).enqueue(object : Callback<ApiResponse> { // Corrected to pass newUser
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    _registrationStatus.value = RegistrationStatus.SUCCESS
                } else {
                    _registrationStatus.value = RegistrationStatus.FAILURE
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _registrationStatus.value = RegistrationStatus.ERROR
            }
        })
    }

    enum class LoginStatus {
        SUCCESS,
        FAILURE,
        ERROR
    }

    enum class RegistrationStatus {
        SUCCESS,
        FAILURE,
        ERROR
    }

    class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
