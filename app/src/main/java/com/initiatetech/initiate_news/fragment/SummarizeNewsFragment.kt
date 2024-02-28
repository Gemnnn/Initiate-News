//package com.initiatetech.initiate_news.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.initiatetech.initiate_news.R
//import com.initiatetech.initiate_news.adapter.NewsTimelineAdapter
//import com.initiatetech.initiate_news.api.RetrofitClient
//import com.initiatetech.initiate_news.databinding.FragmentSummarizeNewsBinding
//import com.initiatetech.initiate_news.model.NewsResponse
//
//
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
//private var _binding: FragmentSummarizeNewsBinding? = null
//
//private lateinit var newsTimelineAdapter: NewsTimelineAdapter
//
//private val binding get() = _binding!!
//
///**
// * A simple [Fragment] subclass.
// * Use the [SummarizeNewsFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class SummarizeNewsFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentSummarizeNewsBinding.inflate(inflater, container, false)
//
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // Retrieve and display the keyword
//        val keyword = arguments?.getString("keyword")
//        view.findViewById<TextView>(R.id.tv_keyword).text = keyword
//
//        binding.btnNewsBack.setOnClickListener {
//            val f = NewsFragment()
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.main_content, f) // Use the correct container ID
//                .addToBackStack(null)
//                .commit()
//        }
//
//
//
//        setupRecyclerView()
//        fetchDataForTimeline(keyword)
//
//    }
//
//
//    private fun fetchDataForTimeline(keyword: String?) {
//        keyword?.let {
//            RetrofitClient.instance.getAllKeywordNews("username", it).enqueue(object : retrofit2.Callback<List<NewsResponse>> {
//                override fun onResponse(call: retrofit2.Call<List<NewsResponse>>, response: retrofit2.Response<List<NewsResponse>>) {
//                    if (response.isSuccessful) {
//                        val newsItems = response.body() ?: emptyList()
//                        (binding.rvTimeline.adapter as NewsTimelineAdapter).submitList(newsItems)
//                    } else {
//                        // Handle request error
//                    }
//                }
//
//                override fun onFailure(call: retrofit2.Call<List<NewsResponse>>, t: Throwable) {
//                    // Handle failure
//                }
//            })
//        }
//    }
//
//
//    private fun setupRecyclerView() {
//        newsTimelineAdapter = NewsTimelineAdapter() // Initialize your adapter here without the list
//
//        with(binding.rvTimeline) {
//            layoutManager = LinearLayoutManager(context)
//            adapter = newsTimelineAdapter
//        }
//
//        // If you need to submit an empty list or any list at the initialization phase
//        newsTimelineAdapter.submitList(emptyList()) // Use this to submit an empty list or any initial data
//    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SummarizeNewsFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SummarizeNewsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}

package com.initiatetech.initiate_news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        val keyword = arguments?.getString("keyword")
        view.findViewById<TextView>(R.id.tv_keyword).text = keyword

        binding.btnNewsBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()

        val username = userViewModel.getUserEmail() ?: "test@test.com"

        if (keyword != null) {
            viewModel.fetchNewsForKeyword(username, keyword)
        }

        viewModel.newsItems.observe(viewLifecycleOwner) { newsItems ->
            // Update your RecyclerView adapter here
            newsTimelineAdapter.submitList(newsItems)
        }
    }

    private fun setupRecyclerView() {
        newsTimelineAdapter = NewsTimelineAdapter()

        with(binding.rvTimeline) {
            layoutManager = LinearLayoutManager(context)
            adapter = newsTimelineAdapter
        }
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
