package com.cmdv.domain.model

data class UserModel(
	val uid: String,
	val email: String,
	val displayName: String,
	val isNew: Boolean?,
	val isAuthenticated: Boolean,
	val isCreated: Boolean?
)