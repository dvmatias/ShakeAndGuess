package com.cmdv.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.model.UserModel
import com.cmdv.domain.repository.SplashRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SplashRepositoryImpl : SplashRepository {

	private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

	private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()

	private val usersRef: CollectionReference = rootRef.collection("users")

	/**
	 *
	 */
	override fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<UserModel> {
		val isUserAuthenticateInFirebaseMutableLiveData = MutableLiveData<UserModel>()
		val firebaseUser = firebaseAuth.currentUser

		val uid: String = firebaseUser?.uid ?: ""
		val email: String = firebaseUser?.email ?: ""
		val displayName: String = firebaseUser?.displayName ?: ""
		val isNew: Boolean? = false
		val isAuthenticated: Boolean = firebaseUser != null
		val isCreated: Boolean? = false

		isUserAuthenticateInFirebaseMutableLiveData.value = UserModel(uid, email, displayName, isNew, isAuthenticated, isCreated)
		return isUserAuthenticateInFirebaseMutableLiveData
	}

	/**
	 *
	 */
	override fun addUserToLiveData(uid: String): MutableLiveData<UserModel> {
		val userMutableLiveData: MutableLiveData<UserModel> = MutableLiveData()
		usersRef.document(uid).get().addOnCompleteListener{userTask ->
			if (userTask.isSuccessful) {
				val document: DocumentSnapshot? = userTask.result
				document?.let {
					if (it.exists()) {
						val user = it.toObject(UserModel::class.java)
						userMutableLiveData.value = user
					}
				}
			} else {
				Log.d("SplashRepositoryImpl", "UserTask ERROR: ${userTask.exception?.message}")
				// TODO handle this error.
				firebaseAuth.signOut()
			}

		}
		return userMutableLiveData
	}
}