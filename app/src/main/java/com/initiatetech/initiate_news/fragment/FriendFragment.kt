package com.initiatetech.initiate_news.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.databinding.FragmentFriendBinding
import com.initiatetech.initiate_news.repository.FriendRepository
import com.initiatetech.initiate_news.viewmodel.FriendViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var _binding: FragmentFriendBinding? = null
private val binding get() = _binding!!
private lateinit var viewModel: FriendViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        // Initialize the ViewModel
        val factory = FriendViewModel.FriendViewModelFactory(FriendRepository(), context)
        viewModel = ViewModelProvider(this, factory).get(FriendViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllFriends()

        viewModel.friends.observe(viewLifecycleOwner) { friends ->
            val friendsListContainer = binding.friendsListContainer
            friendsListContainer.removeAllViews() // Clear existing views

            friends.forEach { friend ->
                val friendView = LayoutInflater.from(context).inflate(R.layout.friend_item, friendsListContainer, false)
                val tvFriend = friendView.findViewById<TextView>(R.id.txt_friend_name)
                val btnFriendStatus = friendView.findViewById<Button>(R.id.btn_friendStatus)
                // TODO: IMPLEMENT A REMOVE FRIEND BUTTON?
                //val btnRemoveFriend = friendView.findViewById<Button>(R.id.btn_friendStatus)

                tvFriend.text = friend
                btnFriendStatus.setOnClickListener {
                    // TODO: MAKE THIS ONLY CLICKABLE IF IT SAYS MESSAGE INSTEAD OF PENDING
//                    viewModel.sendMessage()
                }
//                btnRemoveFriend.setOnClickListener {
//                    // Remove the friend
//                    viewModel.removeFriend()
//                }
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