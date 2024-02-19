package com.example.submissiongithubapp.data.retrofit
import com.example.submissiongithubapp.BuildConfig
import com.example.submissiongithubapp.data.response.UserDetailResponse
import com.example.submissiongithubapp.data.response.UserFollowsResponseItem
import com.example.submissiongithubapp.data.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun getUsersByUsername(
        @Query("q") q: String,
        @Header("Authorization") authorization: String = BuildConfig.BEARER_TOKEN
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    fun getUserDetails(
        @Path("username") username: String,
        @Header("Authorization") authorization: String = BuildConfig.BEARER_TOKEN
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String,
        @Header("Authorization") authorization: String = BuildConfig.BEARER_TOKEN
    ): Call<List<UserFollowsResponseItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String,
        @Header("Authorization") authorization: String = BuildConfig.BEARER_TOKEN
    ): Call<List<UserFollowsResponseItem>>
}