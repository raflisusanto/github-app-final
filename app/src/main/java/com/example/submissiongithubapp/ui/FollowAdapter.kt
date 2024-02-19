package com.example.submissiongithubapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.submissiongithubapp.data.response.UserFollowsResponseItem
import com.example.submissiongithubapp.databinding.ItemUserBinding
import com.example.submissiongithubapp.helper.loadImage

class FollowAdapter : ListAdapter<UserFollowsResponseItem, FollowAdapter.FollowViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class FollowViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserFollowsResponseItem){

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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserFollowsResponseItem>() {
            override fun areItemsTheSame(oldItem: UserFollowsResponseItem, newItem: UserFollowsResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: UserFollowsResponseItem, newItem: UserFollowsResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}