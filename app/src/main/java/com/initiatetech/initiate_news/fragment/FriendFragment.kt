package com.initiatetech.initiate_news.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.databinding.FragmentFriendBinding
import com.initiatetech.initiate_news.login.LoginActivity
import com.initiatetech.initiate_news.repository.FriendRepository
import com.initiatetech.initiate_news.repository.SharedKeywordRepository
import com.initiatetech.initiate_news.viewmodel.FriendViewModel
import com.initiatetech.initiate_news.viewmodel.SharedKeywordViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var _binding: FragmentFriendBinding? = null
private val binding get() = _binding!!
private lateinit var viewModel: FriendViewModel
private lateinit var shareViewModel: SharedKeywordViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val pending = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFriendBinding.inflate(inflater, container, false)

        // TODO: Fix this
        // Initialize the SharedKeywordViewModel
//        val shareFactory = SharedKeywordViewModel.SharedKeywordViewModelFactory(SharedKeywordRepository(), context)
//        shareViewModel = ViewModelProvider(this, shareFactory).get(SharedKeywordViewModel::class.java)
//
//        // Initialize the ViewModel
//        val factory = FriendViewModel.FriendViewModelFactory(FriendRepository(), shareViewModel, context)
//        viewModel = ViewModelProvider(this, factory).get(FriendViewModel::class.java)

        val factory = FriendViewModel.FriendViewModelFactory(FriendRepository(), context)
        viewModel = ViewModelProvider(this, factory).get(FriendViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the add friend textbox and button
        binding.btnAddFriend.setOnClickListener {
            val friendUsername = binding.etAddFriend.text.toString().trim()
            if (friendUsername.isNotEmpty()) {
                viewModel.addFriend(friendUsername)
                binding.etAddFriend.text.clear()
            }
        }

        // Gets all of users current friends and friend requests sent/received
        viewModel.getAllFriends()

        viewModel.friends.observe(viewLifecycleOwner) { friends ->
            val friendsListContainer = binding.friendsListContainer
            friendsListContainer.removeAllViews() // Clear existing views

            friends.forEach { friend ->
                val friendView = LayoutInflater.from(context).inflate(R.layout.friend_item, friendsListContainer, false)
                val tvFriend = friendView.findViewById<TextView>(R.id.txt_friend_name)
                val btnFriendStatus = friendView.findViewById<AppCompatButton>(R.id.btn_friendStatus)

                tvFriend.text = friend.friendUsername

                if (friend.status != pending) { // If request has been accepted...
                    btnFriendStatus.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.primary))
                    btnFriendStatus.setText(R.string.share)

                    btnFriendStatus.setOnClickListener {
                        viewModel.share()
                    }

                } else if (friend.isRequestedByCurrentUser) { // If user sent the request...
                    btnFriendStatus.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.pending))
                    btnFriendStatus.setText(R.string.pending)

                } else { // Else the user is the un-accepted receiver
                    btnFriendStatus.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.accept))
                    btnFriendStatus.setText(R.string.accept)

                    btnFriendStatus.setOnClickListener {
                        viewModel.acceptFriend(friend.friendUsername)
                    }
                }

                // Set up the remove friend 'X' button
                val btnRemoveFriend = friendView.findViewById<ImageButton>(R.id.btn_removeFriend)
                btnRemoveFriend.setOnClickListener {
                    // Remove the friend/friendRequest
                    viewModel.rejectFriend(friend.friendUsername)
                }

                friendsListContainer.addView(friendView)
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}