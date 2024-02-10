package com.initiatetech.initiate_news.login

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.register.RegisterActivity
import com.initiatetech.initiate_news.repository.UserRepository
import com.initiatetech.initiate_news.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the ViewModel
        val factory = UserViewModel.UserViewModelFactory(UserRepository())
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)
        registerButton = findViewById(R.id.btn_register)

        loginButton.setOnClickListener {
            performLogin()
        }

        registerButton.setOnClickListener {
            navigateToRegisterActivity()
        }

        observeLoginStatus()
    }

    private fun performLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.loginUser(email, password)
        } else {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeLoginStatus() {
        viewModel.loginStatus.observe(this, Observer { status ->
            when (status) {
                UserViewModel.LoginStatus.SUCCESS -> {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                }
                UserViewModel.LoginStatus.FAILURE -> {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
                UserViewModel.LoginStatus.ERROR -> {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
