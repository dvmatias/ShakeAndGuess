package com.cmdv.domain.repository

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.model.UserModel

interface SplashRepository {

	fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<UserModel>

	fun addUserToLiveData(uid: String): MutableLiveData<UserModel>

}