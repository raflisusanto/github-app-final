package com.example.submissiongithubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissiongithubapp.data.response.ItemsItem
import com.example.submissiongithubapp.data.response.UserSearchResponse
import com.example.submissiongithubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String> = _errorToast

    private val _searchText = MutableLiveData<String>()

    init {
        findUsers(_searchText.value ?: "rafli")
    }

    fun setSearchText(newSearchText : String) {
        _searchText.value = newSearchText
        findUsers(newSearchText)
    }

    private fun findUsers(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersByUsername(username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUsers.value = responseBody.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorToast.value = response.message()
                }
            }
            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorToast.value = t.message
            }
        })
    }

    companion object{
        private const val TAG = "UsersViewModel"
    }
}