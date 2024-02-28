package com.initiatetech.initiate_news.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.api.RetrofitClient
import com.initiatetech.initiate_news.model.ApiResponse
import com.initiatetech.initiate_news.model.PreferenceData
import com.initiatetech.initiate_news.model.PreferenceResponse
import com.initiatetech.initiate_news.model.User
import com.initiatetech.initiate_news.repository.PreferenceRepository
import com.initiatetech.initiate_news.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

class UserViewModel(private val userRepository: UserRepository,
                    private val preferenceRepository: PreferenceRepository,
                    context: Context?
) : ViewModel() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val context: Context?
        get() = contextRef.get()

    // LiveData for login status
    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> = _loginStatus

    // LiveData for registration status
    private val _registrationStatus = MutableLiveData<RegistrationStatus>()
    val registrationStatus: LiveData<RegistrationStatus> = _registrationStatus

    // LiveData for logout status
    private val _logoutStatus = MutableLiveData<LogoutStatus>()
    val logoutStatus: LiveData<LogoutStatus> = _logoutStatus

    val preferenceUpdateStatus = MutableLiveData<Boolean>()


    fun loginUser(email: String, password: String) {
        val loginInfo = User(email, password) // Assuming UserLogin is similar or use User directly if same attributes
        Log.d("Login", "Start")
        userRepository.loginUser(loginInfo).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    // Store the user's email upon successful login
                    saveUserEmail(loginInfo.email)
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

    private fun saveUserEmail(email: String) {
        val sharedPref = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString("user_email", email)
        editor?.apply()
    }

    public fun getUserEmail(): String? {
        val sharedPref = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("user_email", null)
    }

    fun logoutUser() {
        userRepository.logoutUser().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Log.d("Logout", "success")
                    _logoutStatus.value = LogoutStatus.SUCCESS
                } else {
                    Log.d("Logout", "fail")
                    _logoutStatus.value = LogoutStatus.FAILURE
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _logoutStatus.value = LogoutStatus.ERROR
            }
        })
    }



    fun registerUser(email: String, password: String) {
        val newUser = User(email, password) // Corrected to create a new User instance
        Log.d("Registration", "Start")
        userRepository.registerUser(newUser).enqueue(object : Callback<ApiResponse> { // Corrected to pass newUser
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Log.d("Registration", "success")
                    _registrationStatus.value = RegistrationStatus.SUCCESS
                } else {
                    Log.d("Registration", "fail")
                    _registrationStatus.value = RegistrationStatus.FAILURE
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _registrationStatus.value = RegistrationStatus.ERROR
            }
        })
    }

    fun getPreferences(callback: (PreferenceResponse?) -> Unit) {
        val userEmail = getUserEmail()
        if (userEmail != null) {
            RetrofitClient.instance.getPreferences(userEmail).enqueue(object : Callback<PreferenceResponse> {
                override fun onResponse(call: Call<PreferenceResponse>, response: Response<PreferenceResponse>) {
                    if (response.isSuccessful) {
                        // Directly pass the PreferenceResponse object to the callback
                        callback(response.body())
                    } else {
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<PreferenceResponse>, t: Throwable) {
                    callback(null)
                }
            })
        } else {
            callback(null)
        }
    }


    fun setPreferences(language: String, province: String, country: String, newsGenerationTime: String, isSetPreference: Boolean) {
        val preferences = PreferenceData(language, province, country, newsGenerationTime, getUserEmail(), isSetPreference)
        Log.d("Preferences", "setPreferences Start")
        preferenceRepository.setPreferences(preferences).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Log.d("Preferences", "setPreferences Successful response")
                    preferenceUpdateStatus.postValue(true)
                } else {
                    Log.d("Preferences", "setPreferences failed response")
                    preferenceUpdateStatus.postValue(false)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Preferences", "setPreferences Error", t)
                preferenceUpdateStatus.postValue(false)
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

    enum class LogoutStatus {
        SUCCESS,
        FAILURE,
        ERROR
    }

    class UserViewModelFactory(private val userRepository: UserRepository,
                               private val preferenceRepository: PreferenceRepository,
                               private val context: Context?
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(userRepository, preferenceRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
