package com.initiatetech.initiate_news.register

import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.repository.UserRepository
import com.initiatetech.initiate_news.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the ViewModel
        val factory = UserViewModel.UserViewModelFactory(UserRepository())
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        registerButton = findViewById(R.id.buttonRegister)

        registerButton.setOnClickListener {
            registerUser()
        }

        observeRegistrationStatus()
    }

    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Example validation, adjust according to your requirements
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) { // Example minimum length
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.registerUser(email, password)
        }
    }

    private fun observeRegistrationStatus() {
        viewModel.registrationStatus.observe(this, Observer { status ->
            when (status) {
                UserViewModel.RegistrationStatus.SUCCESS -> {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                }
                UserViewModel.RegistrationStatus.FAILURE -> {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
                UserViewModel.RegistrationStatus.ERROR -> {
                    Toast.makeText(this, "An error occurred during registration", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
