package com.cmdv.screen.fragments.authentication

import androidx.lifecycle.MutableLiveData
import com.cmdv.core.base.BaseMVVMViewModel
import com.cmdv.domain.model.UserModel
import com.cmdv.domain.repository.AuthenticationRepository
import com.google.firebase.auth.AuthCredential

class AuthenticationFragmentViewModel(
	private val authenticationRepository: AuthenticationRepository
) : BaseMVVMViewModel() {

	var authenticatedUserLiveData = MutableLiveData<UserModel>()
	var createdUserLiveData = MutableLiveData<UserModel>()

	fun signInWithGoogle(googleAuthCredential: AuthCredential) {
		authenticatedUserLiveData =
			authenticationRepository.firebaseSignInWithGoogle(googleAuthCredential)
	}

	fun createUser(authenticatedUser: UserModel) {
		createdUserLiveData =
			authenticationRepository.createUserInFirestoreIfNotExists(authenticatedUser)
	}

}