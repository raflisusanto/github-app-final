package com.example.submissiongithubapp.data.response

import com.google.gson.annotations.SerializedName

data class UserFollowsResponseItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("html_url")
	val htmlUrl: String,
)
