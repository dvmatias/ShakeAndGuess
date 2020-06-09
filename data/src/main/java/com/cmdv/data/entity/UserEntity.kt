package com.cmdv.data.entity

import com.google.gson.annotations.SerializedName

data class UserEntity (
	@SerializedName("uid") val uid: String,
	@SerializedName("email") val email: String,
	@SerializedName("name") val name: String
)