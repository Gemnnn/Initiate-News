package com.initiatetech.initiate_news.fragment

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.adapter.NewsTimelineAdapter
import com.initiatetech.initiate_news.databinding.FragmentSummarizeNewsBinding
import com.initiatetech.initiate_news.repository.NewsRepository
import com.initiatetech.initiate_news.repository.PreferenceRepository
import com.initiatetech.initiate_news.repository.UserRepository
import com.initiatetech.initiate_news.viewmodel.NewsViewModel
import com.initiatetech.initiate_news.viewmodel.UserViewModel


class SummarizeNewsFragment : Fragment() {

    private var _binding: FragmentSummarizeNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsTimelineAdapter: NewsTimelineAdapter

    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private lateinit var viewModel: NewsViewModel

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSummarizeNewsBinding.inflate(inflater, container, false)
        val factory = NewsViewModel.NewsViewModelFactory(NewsRepository())
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)

        val userFactory = UserViewModel.UserViewModelFactory(UserRepository(), PreferenceRepository(), context)
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val keyword = arguments?.getString("keyword") ?: return
        view.findViewById<TextView>(R.id.tv_keyword).text = keyword

        binding.btnNewsBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()

        val username = userViewModel.getUserEmail() ?: "test@gmail.com"

        if (keyword != null) {
            viewModel.fetchNewsForKeyword(username, keyword)
        }

        viewModel.newsItems.observe(viewLifecycleOwner) { newsItems ->
            if (newsItems.isEmpty()) {
                // If there are no news items, show the "no news" message
                binding.tvNoNewsMessage.visibility = View.VISIBLE
                binding.tvNoNewsMessage.text = getString(R.string.no_news_message, keyword)
                binding.rvTimeline.visibility = View.GONE
            } else {
                // If there are news items, show them in the RecyclerView
                binding.tvNoNewsMessage.visibility = View.GONE
                binding.rvTimeline.visibility = View.VISIBLE
                newsTimelineAdapter.submitList(newsItems)

                // Auto Scroll
                val smoothScroller = CustomSmoothScroller(requireContext())
                smoothScroller.targetPosition = newsItems.size - 1
                binding.rvTimeline.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }

    }
    class CustomSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 50f / displayMetrics.densityDpi
        }
    }

    private fun setupRecyclerView() {
        // This lambda matches the adapter's constructor parameter
        newsTimelineAdapter = NewsTimelineAdapter { newsId ->
            navigateToNewsDetailFragment(newsId)
        }

        binding.rvTimeline.layoutManager = LinearLayoutManager(context)
        binding.rvTimeline.adapter = newsTimelineAdapter
    }

    private fun navigateToNewsDetailFragment(newsId: String) {
        val fragment = NewsDetailFragment().apply {
            arguments = Bundle().apply {
                putString("newsId", newsId)
            }
        }
        fragment.show(parentFragmentManager, "NewsDetailFragment")
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SummarizeNewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
