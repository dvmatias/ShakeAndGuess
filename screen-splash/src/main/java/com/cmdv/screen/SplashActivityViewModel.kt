package com.cmdv.screen

import androidx.lifecycle.MutableLiveData
import com.cmdv.core.base.BaseMVVMViewModel
import com.cmdv.domain.model.UserModel
import com.cmdv.domain.repository.SplashRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

class SplashActivityViewModel(private val splashRepository: SplashRepository) : BaseMVVMViewModel() {
	var googleSinInTask = MutableLiveData<Task<GoogleSignInAccount>>()
	var isUserAuthenticatedLiveData = MutableLiveData<UserModel>()
	var userLiveData = MutableLiveData<UserModel>()

	fun checkIfUserIsAuthenticated() {
		isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase()
	}

	fun setUid(uid: String) {
		userLiveData = splashRepository.addUserToLiveData(uid)
	}

}