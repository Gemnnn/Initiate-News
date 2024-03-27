package com.initiatetech.initiate_news.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.databinding.FragmentSharedKeywordsBinding
import com.initiatetech.initiate_news.repository.SharedKeywordRepository
import com.initiatetech.initiate_news.viewmodel.SharedKeywordViewModel


class SharedKeywordFragment : Fragment() {

    private var _binding: FragmentSharedKeywordsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedKeywordViewModel // Is this okay or no?

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSharedKeywordsBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        val factory = SharedKeywordViewModel.SharedKeywordViewModelFactory(SharedKeywordRepository(), context)
        viewModel = ViewModelProvider(this, factory).get(SharedKeywordViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gets all of keywords shared with the user
        viewModel.getSharedKeywords()

        viewModel.sharedKeywords.observe(viewLifecycleOwner) { sharedKeywords ->
            val sharedKeywordsListContainer = binding.sharedKeywordsListContainer
            sharedKeywordsListContainer.removeAllViews() // Clear existing views

            sharedKeywords.forEach { sharedKeyword ->
                val sharedKeywordView = LayoutInflater.from(context).inflate(R.layout.item_shared_keyword, sharedKeywordsListContainer, false)
//                val tvFriend = friendView.findViewById<TextView>(R.id.txt_friend_name)
//                val btnFriendStatus = friendView.findViewById<AppCompatButton>(R.id.btn_friendStatus)


                sharedKeywordsListContainer.addView(sharedKeywordView)
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FriendFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}