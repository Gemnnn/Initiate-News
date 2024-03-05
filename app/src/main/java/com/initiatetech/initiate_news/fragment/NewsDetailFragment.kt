package com.initiatetech.initiate_news.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.repository.NewsRepository
import com.initiatetech.initiate_news.repository.PreferenceRepository
import com.initiatetech.initiate_news.repository.UserRepository
import com.initiatetech.initiate_news.viewmodel.NewsViewModel
import com.initiatetech.initiate_news.viewmodel.UserViewModel

class NewsDetailFragment : DialogFragment() {

    private lateinit var viewModel: NewsViewModel

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.NewsDetailDialog)

        // Initialize the ViewModel
        val factory = NewsViewModel.NewsViewModelFactory(NewsRepository())
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)

        val userFactory = UserViewModel.UserViewModelFactory(UserRepository(), PreferenceRepository(), context)
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assuming 'newsId' is passed as a string argument; parse it to Int
        val newsId = arguments?.getString("newsId")?.toIntOrNull()
        val username = userViewModel.getUserEmail() ?: "test@test.com"

        newsId?.let {
            viewModel.fetchNewsDetail(username, it)
        }

        // Observe the newsDetail LiveData
        viewModel.newsDetail.observe(viewLifecycleOwner, { newsDetail ->
            // Update UI with news details
            newsDetail?.let { detail ->
                view.findViewById<TextView>(R.id.tvNewsTitle).text = detail.title
                view.findViewById<TextView>(R.id.tvNewsShortTitle).text = detail.shortTitle
                val sourceTextView = view.findViewById<TextView>(R.id.tvNewsSource)
                sourceTextView.text = detail.sourceUrl
                sourceTextView.setOnClickListener {
                    openUrlInBrowser(detail.sourceUrl)
                }
                view.findViewById<TextView>(R.id.tvPublishedDateAuthor).text = "${detail.publishedDate} / ${detail.author}"
                view.findViewById<TextView>(R.id.tvNewsContent).text = detail.content
            } ?: run {
                // Handle null case or error in fetching news details
            }
        })
    }

    private fun openUrlInBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open URL", Toast.LENGTH_SHORT).show()
        }
    }
}
