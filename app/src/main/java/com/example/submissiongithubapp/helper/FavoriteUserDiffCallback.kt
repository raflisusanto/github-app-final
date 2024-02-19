package com.example.submissiongithubapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.submissiongithubapp.database.FavoriteUser

class FavoriteUserDiffCallback(private val oldFavUserList: List<FavoriteUser>, private val newFavUserList: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavUserList.size
    override fun getNewListSize(): Int = newFavUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Username is used like 'id'
        return oldFavUserList[oldItemPosition].username == newFavUserList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldFavUserList[oldItemPosition]
        val newNote = newFavUserList[newItemPosition]
        return oldNote.description == newNote.description && oldNote.avatar == newNote.avatar
    }
}