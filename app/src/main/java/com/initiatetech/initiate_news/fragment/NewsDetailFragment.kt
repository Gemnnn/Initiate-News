package com.initiatetech.initiate_news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.initiatetech.initiate_news.R

class NewsDetailFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.NewsDetailDialog)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        arguments?.getString("newsId")?.let { newsId ->
//            // Use newsId to fetch and display
//        }

        val newsId = arguments?.getString("newsId")

        view.findViewById<TextView>(R.id.tvNewsId).text = "News ID: $newsId"
    }

}