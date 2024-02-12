package com.initiatetech.initiate_news

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.initiatetech.initiate_news.databinding.ActivityMainBinding
import android.os.Bundle
import com.initiatetech.initiate_news.fragment.FriendFragment
import com.initiatetech.initiate_news.fragment.NewsFragment
import com.initiatetech.initiate_news.fragment.SearchFragment
import com.initiatetech.initiate_news.fragment.UserFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initially displays the NewsFragment automatically
        supportFragmentManager.beginTransaction().replace(R.id.main_content, NewsFragment()).commit()

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_home ->{
                    val f = NewsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_search ->{
                    val f = SearchFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_friend ->{
                    val f = FriendFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_account ->{
                    val f = UserFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }
}