package com.initiatetech.initiate_news.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.initiatetech.initiate_news.MainActivity
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var btn_login : Button

//    val btn_login:Button = findViewById(R.id.btn_login)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login = findViewById(R.id.btn_login)

        btn_login.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}