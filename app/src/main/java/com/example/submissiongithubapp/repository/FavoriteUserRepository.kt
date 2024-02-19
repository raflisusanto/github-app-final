package com.example.submissiongithubapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.submissiongithubapp.database.FavoriteUser
import com.example.submissiongithubapp.database.FavoriteUserDao
import com.example.submissiongithubapp.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavUserDao = db.favUserDao()
    }

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavUserDao.getAllFavUser()
    fun getUsersByUsername(username: String): LiveData<FavoriteUser> = mFavUserDao.getUsersByUsername(username)

    fun insert(favUser: FavoriteUser) {
        executorService.execute { mFavUserDao.insert(favUser) }
    }
    fun delete(favUser: FavoriteUser) {
        executorService.execute { mFavUserDao.delete(favUser) }
    }
}