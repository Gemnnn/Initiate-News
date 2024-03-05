package com.initiatetech.initiate_news

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.initiatetech.initiate_news.databinding.ActivitySplashBinding
import com.initiatetech.initiate_news.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val texts = arrayOf("Simplified", "Stay Informed", "Personalized")
    private var textIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
        }, 500) // 300 milliseconds delay

        animateText()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000) // 3.5 seconds delay
    }

    private fun animateText() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (textIndex < texts.size) {
                    binding.textViewAnimated.apply {
                        startAnimation(fadeInAnimation)
                        text = texts[textIndex++]
                        startAnimation(fadeOutAnimation)
                    }
                    handler.postDelayed(this, 1500) // Delay to accommodate animations
                }
            }
        }
        handler.post(runnable)
    }

    private val fadeInAnimation: Animation = AlphaAnimation(0.0f, 1.0f).apply {
        duration = 1100
        fillAfter = true
    }

    private val fadeOutAnimation: Animation = AlphaAnimation(1.0f, 0.0f).apply {
        duration = 1100
        fillAfter = true
    }

}
