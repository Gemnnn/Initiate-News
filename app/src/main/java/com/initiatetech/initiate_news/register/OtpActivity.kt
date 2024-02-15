package com.initiatetech.initiate_news.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.databinding.ActivityOtpBinding
import com.initiatetech.initiate_news.login.LoginActivity
import com.initiatetech.initiate_news.repository.PreferenceRepository
import com.initiatetech.initiate_news.repository.UserRepository
import com.initiatetech.initiate_news.viewmodel.UserViewModel
import papaya.`in`.sendmail.SendMail
import kotlin.random.Random
import kotlin.random.nextInt

class OtpActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    lateinit var binding : ActivityOtpBinding
    lateinit var auth: FirebaseAuth
    var userEmail : String = ""
    var userPassword : String = ""
    var random: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)
        userEmail = intent.getStringExtra("email").toString()
        userPassword = intent.getStringExtra("pass").toString()
        auth = FirebaseAuth.getInstance()

        random()

        binding.tvShowEmail.setText(userEmail)

        binding.tvResend.setOnClickListener {
            random()
        }

        val factory = UserViewModel.UserViewModelFactory(UserRepository(), PreferenceRepository(), this)
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)


        binding.etOtp1.doOnTextChanged { text, start, before, count ->
            if(!binding.etOtp1.text.toString().isEmpty()){
                binding.etOtp2.requestFocus()
            }
            if(!binding.etOtp2.text.toString().isEmpty()){
                binding.etOtp2.requestFocus()
            }
        }

        binding.etOtp2.doOnTextChanged { text, start, before, count ->
            if(!binding.etOtp2.text.toString().isEmpty()){
                binding.etOtp3.requestFocus()
            }
            else{
                binding.etOtp1.requestFocus()
            }
        }

        binding.etOtp3.doOnTextChanged { text, start, before, count ->
            if(!binding.etOtp3.text.toString().isEmpty()){
                binding.etOtp4.requestFocus()
            }
            else{
                binding.etOtp2.requestFocus()
            }
        }

        binding.etOtp4.doOnTextChanged { text, start, before, count ->
            if(!binding.etOtp4.text.toString().isEmpty()){
                binding.etOtp5.requestFocus()
            }
            else{
                binding.etOtp3.requestFocus()
            }
        }

        binding.etOtp5.doOnTextChanged { text, start, before, count ->
            if(!binding.etOtp5.text.toString().isEmpty()){
                binding.etOtp6.requestFocus()
            }
            else{
                binding.etOtp4.requestFocus()
            }
        }

        binding.etOtp6.doOnTextChanged { text, start, before, count ->
            if(binding.etOtp6.text.toString().isEmpty()){
                binding.etOtp5.requestFocus()
            }

            binding.btnOtpDone.setOnClickListener{
                var otp1=binding.etOtp1.text.toString()
                var otp2=binding.etOtp2.text.toString()
                var otp3=binding.etOtp3.text.toString()
                var otp4=binding.etOtp4.text.toString()
                var otp5=binding.etOtp5.text.toString()
                var otp6=binding.etOtp6.text.toString()

                var otp="$otp1$otp2$otp3$otp4$otp5$otp6"

                if(binding.etOtp1.text.toString().isEmpty()||
                    binding.etOtp2.text.toString().isEmpty()||
                    binding.etOtp3.text.toString().isEmpty()||
                    binding.etOtp4.text.toString().isEmpty()||
                    binding.etOtp5.text.toString().isEmpty()||
                    binding.etOtp6.text.toString().isEmpty()){
                    Toast.makeText(this@OtpActivity, "Enter OTP", Toast.LENGTH_SHORT).show()
                }
                else if(!otp.equals(random.toString())){
                    Toast.makeText(this@OtpActivity, "Wrong OTP", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener {
                        // OTP is correct
                        if(it.isSuccessful){
                            viewModel.registerUser(userEmail, userPassword)
                            observeRegistrationStatus()
                        }
                        else {
                            Toast.makeText(this@OtpActivity, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    fun random() {
        random = Random.nextInt(100000..999999) // Generate a random number between 100000 and 999999
        val mail = SendMail(
            "initiatenews@gmail.com",
            "erkqqljissuduwzf",
            userEmail,
            "Initiate News OTP",
            "Your OTP is -> $random"
        )
        mail.execute()
        Log.d("Registration", "Sent OTP Mail")
    }

    private fun observeRegistrationStatus() {
        viewModel.registrationStatus.observe(this, Observer { status ->
            when (status) {
                UserViewModel.RegistrationStatus.SUCCESS -> {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    navigateToLoginActivity()
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

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Log.d("Registration", "navigated to login")
    }
}