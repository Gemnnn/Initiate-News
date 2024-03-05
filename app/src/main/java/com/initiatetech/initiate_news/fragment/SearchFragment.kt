package com.initiatetech.initiate_news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.databinding.FragmentSearchBinding
import com.initiatetech.initiate_news.repository.KeywordRepository
import com.initiatetech.initiate_news.repository.NewsRepository
import com.initiatetech.initiate_news.viewmodel.KeywordViewModel


private var _binding : FragmentSearchBinding? = null
private val binding get() = _binding!!

private lateinit var viewModel: KeywordViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Initialize View Model
        val factory = KeywordViewModel.KeywordViewModelFactory(KeywordRepository(), NewsRepository(), context)
        viewModel = ViewModelProvider(this, factory).get(KeywordViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddKeyword.setOnClickListener {
            val keyword = binding.etAddKeyword.text.toString().trim()
            if (keyword.isNotEmpty()) {
                viewModel.addKeyword(keyword)
                binding.etAddKeyword.text.clear() // Clear the EditText after adding
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }


}