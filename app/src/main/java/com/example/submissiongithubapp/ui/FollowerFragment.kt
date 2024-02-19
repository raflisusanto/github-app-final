package com.example.submissiongithubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiongithubapp.data.response.UserFollowsResponseItem
import com.example.submissiongithubapp.databinding.FragmentFollowerBinding
import com.example.submissiongithubapp.viewmodel.UserDetailViewModel

class FollowerFragment : Fragment() {
    private lateinit var binding: FragmentFollowerBinding
    private lateinit var userDetailViewModel : UserDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView layout
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        // Initialize MainViewModel observer
        initializeUserViewModel()
    }

    private fun setFollowersData(followList: List<UserFollowsResponseItem>) {
        val adapter = FollowAdapter()
        adapter.submitList(followList)
        binding.rvFollowers.adapter = adapter
    }

    private fun showFollowersLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun initializeUserViewModel() {
        userDetailViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[UserDetailViewModel::class.java]

        userDetailViewModel.userFollowers.observe(requireActivity()) { userFollowers ->
            userFollowers?.let {
                setFollowersData(userFollowers)
            }
        }

        userDetailViewModel.isLoadingFollowers.observe(requireActivity()) {
            showFollowersLoading(it)
        }

        // Error message handling
        userDetailViewModel.errorToast.observe(requireActivity()) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}