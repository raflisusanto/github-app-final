package com.example.submissiongithubapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissiongithubapp.data.response.UserDetailResponse
import com.example.submissiongithubapp.data.response.UserFollowsResponseItem
import com.example.submissiongithubapp.data.retrofit.ApiConfig
import com.example.submissiongithubapp.database.FavoriteUser
import com.example.submissiongithubapp.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel() {

    // Favorite Users
    private val mFavUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insertFavUser(favUser: FavoriteUser) {
        _isUserFavorite.value = true
        mFavUserRepository.insert(favUser)
    }

    fun deleteFavUser(favUser: FavoriteUser) {
        _isUserFavorite.value = false
        mFavUserRepository.delete(favUser)
    }

    fun getIsUserFavorite(username: String) : LiveData<Boolean> {
        val curFavUser = mFavUserRepository.getUsersByUsername(username)

        // Have to observe for LiveData from query because async
        curFavUser.observeForever { favUser ->
            _isUserFavorite.value = favUser != null
        }

        return _isUserFavorite
    }

    private val _isUserFavorite = MutableLiveData<Boolean>()
    val isUserFavorite: MutableLiveData<Boolean> = _isUserFavorite

    // User Details
    private val _userDetail = MutableLiveData<UserDetailResponse?>()
    val userDetail: MutableLiveData<UserDetailResponse?> = _userDetail

    private val _userFollowers = MutableLiveData<List<UserFollowsResponseItem>?>()
    val userFollowers: MutableLiveData<List<UserFollowsResponseItem>?> = _userFollowers

    private val _userFollowing = MutableLiveData<List<UserFollowsResponseItem>?>()
    val userFollowing: MutableLiveData<List<UserFollowsResponseItem>?> = _userFollowing

    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    private val _isLoadingDetails = MutableLiveData<Boolean>()
    val isLoadingDetails: LiveData<Boolean> = _isLoadingDetails

    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String> = _errorToast

    private val _username = MutableLiveData<String>()

    fun setUsername(newUsername : String) {
        // Check if same so that it doesn't hit api again
        if (newUsername != _username.value) {
            _username.value = newUsername
            getUserDetail(newUsername)
            getUserFollowers(newUsername)
            getUserFollowing(newUsername)
        }
    }

    private fun getUserDetail(username : String) {
        _isLoadingDetails.value = true
        val client = ApiConfig.getApiService().getUserDetails(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoadingDetails.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userDetail.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorToast.value = response.message()
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoadingDetails.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorToast.value = t.message
            }
        })
    }

    private fun getUserFollowers(username : String) {
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserFollowsResponseItem>> {
            override fun onResponse(
                call: Call<List<UserFollowsResponseItem>>,
                response: Response<List<UserFollowsResponseItem>>
            ) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userFollowers.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorToast.value = response.message()
                }
            }

            override fun onFailure(call: Call<List<UserFollowsResponseItem>>, t: Throwable) {
                _isLoadingFollowers.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorToast.value = t.message
            }
        })
    }

    private fun getUserFollowing(username : String) {
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserFollowsResponseItem>> {
            override fun onResponse(
                call: Call<List<UserFollowsResponseItem>>,
                response: Response<List<UserFollowsResponseItem>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userFollowing.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorToast.value = response.message()
                }
            }

            override fun onFailure(call: Call<List<UserFollowsResponseItem>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorToast.value = t.message
            }
        })
    }

    companion object{
        private const val TAG = "UserDetailViewModel"
    }
}