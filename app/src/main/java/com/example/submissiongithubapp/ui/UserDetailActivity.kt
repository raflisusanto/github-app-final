package com.example.submissiongithubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.submissiongithubapp.R
import com.example.submissiongithubapp.data.response.UserDetailResponse
import com.example.submissiongithubapp.database.FavoriteUser
import com.example.submissiongithubapp.databinding.ActivityUserDetailBinding
import com.example.submissiongithubapp.helper.ViewModelFactory
import com.example.submissiongithubapp.helper.loadImage
import com.example.submissiongithubapp.viewmodel.UserDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userDetailViewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra(UserAdapter.EXTRA_USERNAME)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUserDetailViewModel(username)

        // Initialize FAB Icon based on db favUser data
        binding.fabAdd.setOnClickListener {
            handleFabClick()
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager = binding.viewPager

        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun setUserDetails(userDetail: UserDetailResponse) {
        val followersText = resources.getString(R.string.followers_count, userDetail.followers)
        val followingText = resources.getString(R.string.following_count, userDetail.following)

        with(binding) {
            tvUserFollowers.text = followersText
            tvUserFollowing.text = followingText
            tvName.text = userDetail.name ?: "Tidak ada nama"
            tvUsername.text = userDetail.login

            // Set picture
            ivUserProfile.loadImage(userDetail.avatarUrl)
        }
    }

    private fun handleFabClick() {
        val userDetail = userDetailViewModel.userDetail.value
        if (userDetail != null) {
            // Check if the user already exists in the database
            val isFavorite = userDetailViewModel.isUserFavorite.value
            val curUser =
                FavoriteUser(userDetail.login, userDetail.htmlUrl, userDetail.avatarUrl)

            if (isFavorite != null && isFavorite) {
                binding.fabAdd.setImageResource(R.drawable.ic_favorite_border)
                userDetailViewModel.deleteFavUser(curUser)
                showToast("User dihapus dari favorit")
            } else {
                binding.fabAdd.setImageResource(R.drawable.ic_favorite)
                userDetailViewModel.insertFavUser(curUser)
                showToast("User ditambah ke favorit")
            }
        }
    }

    private fun showDetailsLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    private fun initializeUserDetailViewModel(username: String?) {
        val factory = ViewModelFactory.getInstance((this@UserDetailActivity).application)
        userDetailViewModel = ViewModelProvider(
            this@UserDetailActivity, factory
        )[UserDetailViewModel::class.java]

        userDetailViewModel.setUsername(username ?: "raflisusanto")
        userDetailViewModel.getIsUserFavorite(username ?: "raflisusanto")

        userDetailViewModel.userDetail.observe(this@UserDetailActivity) { userDetails ->
            userDetails?.let{
                setUserDetails(userDetails)
            }
        }

        userDetailViewModel.isLoadingDetails.observe(this@UserDetailActivity) {
            showDetailsLoading(it)
        }

        // Error message handling
        userDetailViewModel.errorToast.observe(this@UserDetailActivity) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this@UserDetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        userDetailViewModel.isUserFavorite.observe(this@UserDetailActivity) { isFavorite ->
            if (isFavorite) binding.fabAdd.setImageResource(R.drawable.ic_favorite)
            else binding.fabAdd.setImageResource(R.drawable.ic_favorite_border)

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@UserDetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_follower,
            R.string.tab_text_following
        )
    }
}