package com.example.submissiongithubapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.submissiongithubapp.data.response.ItemsItem
import com.example.submissiongithubapp.databinding.ItemUserBinding
import com.example.submissiongithubapp.helper.loadImage

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        // Set Listener to Navigate to Details
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val goToUserDetailsActivity = Intent(context, UserDetailActivity::class.java)
            goToUserDetailsActivity.putExtra(EXTRA_USERNAME, user.login)
            context.startActivity(goToUserDetailsActivity)
        }
    }

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem){
            with(binding) {
                // "login" is the username
                tvUsername.text = user.login

                // Use htmlUrl as the description
                tvUserDescription.text = user.htmlUrl

                // Load user pic with glide
                ivUserPicture.loadImage(user.avatarUrl)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }

        var EXTRA_USERNAME = "extra_username"
    }
}