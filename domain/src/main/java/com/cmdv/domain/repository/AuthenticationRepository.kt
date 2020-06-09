package com.cmdv.domain.repository

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.model.UserModel
import com.google.firebase.auth.AuthCredential

interface AuthenticationRepository {

	fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential): MutableLiveData<UserModel>

	fun createUserInFirestoreIfNotExists(authenticatedUser: UserModel): MutableLiveData<UserModel>

}