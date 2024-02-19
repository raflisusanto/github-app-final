package com.example.submissiongithubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiongithubapp.data.response.UserFollowsResponseItem
import com.example.submissiongithubapp.databinding.FragmentFollowingBinding
import com.example.submissiongithubapp.viewmodel.UserDetailViewModel

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var userDetailViewModel : UserDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView layout
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowing.layoutManager = layoutManager

        // Initialize MainViewModel observer
        initializeUserViewModel()
    }

    private fun setFollowersData(followList: List<UserFollowsResponseItem>) {
        val adapter = FollowAdapter()
        adapter.submitList(followList)
        binding.rvFollowing.adapter = adapter
    }

    private fun showFollowingLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun initializeUserViewModel() {
        userDetailViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[UserDetailViewModel::class.java]

        userDetailViewModel.userFollowing.observe(requireActivity()) { userFollowing ->
            userFollowing?.let {
                setFollowersData(userFollowing)
            }
        }

        userDetailViewModel.isLoadingFollowing.observe(requireActivity()) {
            showFollowingLoading(it)
        }

        // Error message handling
        userDetailViewModel.errorToast.observe(requireActivity()) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}