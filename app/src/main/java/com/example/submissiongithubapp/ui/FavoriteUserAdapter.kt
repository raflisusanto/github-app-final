package com.example.submissiongithubapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.submissiongithubapp.database.FavoriteUser
import com.example.submissiongithubapp.databinding.ItemUserBinding
import com.example.submissiongithubapp.helper.FavoriteUserDiffCallback
import com.example.submissiongithubapp.helper.loadImage

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.FavUserViewHolder>(){
    private val favUsers = ArrayList<FavoriteUser>()

    fun setFavUsers(listNotes: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallback(this.favUsers, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.favUsers.clear()
        this.favUsers.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavUserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavUserViewHolder, position: Int) {
        val user = favUsers[position]
        holder.bind(user)

        // Set Listener to Navigate to Details
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val goToUserDetailsActivity = Intent(context, UserDetailActivity::class.java)
            goToUserDetailsActivity.putExtra(UserAdapter.EXTRA_USERNAME, user.username)
            context.startActivity(goToUserDetailsActivity)
        }
    }

    override fun getItemCount(): Int {
        return favUsers.size
    }

    inner class FavUserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favUser: FavoriteUser) {
            with(binding) {
                tvUsername.text = favUser.username
                tvUserDescription.text = favUser.description
                // Load user pic with glide

                favUser.avatar?.let {
                    ivUserPicture.loadImage(favUser.avatar!!)
                }
            }
        }
    }
}