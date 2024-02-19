package com.example.submissiongithubapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submissiongithubapp.database.FavoriteUser
import com.example.submissiongithubapp.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavUserRepository.getAllFavUser()
}