package com.initiatetech.initiate_news.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.databinding.FragmentNewsBinding
import com.initiatetech.initiate_news.repository.KeywordRepository
import com.initiatetech.initiate_news.repository.NewsRepository
import com.initiatetech.initiate_news.viewmodel.KeywordViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var _binding: FragmentNewsBinding? = null
private val binding get() = _binding!!
private lateinit var viewModel: KeywordViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */



class NewsFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: KeywordViewModel
    private var keywordsRotationJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        val factory = KeywordViewModel.KeywordViewModelFactory(KeywordRepository(), NewsRepository(), context)
        viewModel = ViewModelProvider(this, factory).get(KeywordViewModel::class.java)

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_news, container, false)
        return binding.root
    }


    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gets all of user's current keywords
        viewModel.getKeywords()

        viewModel.keywords.observe(viewLifecycleOwner) { keywords ->
            val keywordsContainer = binding.keywordsContainer
            keywordsContainer.removeAllViews() // Clear existing views

            keywords.forEach { keyword ->
                val keywordView = LayoutInflater.from(context).inflate(R.layout.keyword_item, keywordsContainer, false)
                val tvKeyword = keywordView.findViewById<TextView>(R.id.tvKeyword)
                val btnRemoveKeyword = keywordView.findViewById<ImageButton>(R.id.btnRemoveKeyword)

                tvKeyword.text = keyword
                tvKeyword.setOnClickListener {
                    // Navigate to SummarizeNewsFragment with the keyword
                    navigateToSummarizeNewsFragment(keyword)
                }
                btnRemoveKeyword.setOnClickListener {
                    // Remove the keyword
                    viewModel.removeKeyword(keyword)
                }
                keywordsContainer.addView(keywordView)

                viewModel.keywords.observe(viewLifecycleOwner) { keywords ->
                    updateRotatingKeywords(keywords)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        keywordsRotationJob?.cancel()
    }

    private fun updateRotatingKeywords(keywords: List<String>) {
        keywordsRotationJob?.cancel()
        keywordsRotationJob = viewLifecycleOwner.lifecycleScope.launch {
            var index = 0
            while (true) {
                val textToShow = if (keywords.isNotEmpty()) keywords[index % keywords.size] else "There is no keyword added"
                binding.btnLocationNews?.text = textToShow
                delay(1000) // Rotate
                index++
            }
        }
    }

    private fun navigateToSummarizeNewsFragment(keyword: String) {
        val fragment = SummarizeNewsFragment().apply {
            arguments = Bundle().apply {
                putString("keyword", keyword) // Pass the keyword to the fragment
            }
        }
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.main_content, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }




}
