package com.cmdv.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.model.UserModel
import com.cmdv.domain.repository.AuthenticationRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class AuthenticationRepositoryImpl : AuthenticationRepository {

	private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

	private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()

	private val usersRef: CollectionReference = rootRef.collection("users")

	/**
	 *
	 */
	override fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential): MutableLiveData<UserModel> {
		val authenticatedUserMutableLiveData = MutableLiveData<UserModel>()

		firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener { authTask ->
			if (authTask.isSuccessful) {
				val isNewUser = authTask.result?.additionalUserInfo?.isNewUser ?: false
				val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
				firebaseUser?.let {
					val uid: String = it.uid
					val email: String = it.email ?: ""
					val displayName: String = it.displayName ?: ""
					val isNew: Boolean? = isNewUser
					val isAuthenticated: Boolean = false
					val isCreated: Boolean? = false
					authenticatedUserMutableLiveData.value = UserModel(uid, email, displayName, isNew, isAuthenticated, isCreated)
				}
			} else {
				Log.d(AuthenticationRepositoryImpl::class.java.simpleName, "UserTask ERROR: ${authTask.exception?.message}")
			}
		}

		return authenticatedUserMutableLiveData
	}

	/**
	 *
	 */
	override fun createUserInFirestoreIfNotExists(authenticatedUser: UserModel): MutableLiveData<UserModel> {
		val newUserMutableLiveData = MutableLiveData<UserModel>()
		val uidRef = usersRef.document(authenticatedUser.uid)
		uidRef.get().addOnCompleteListener { uidTask: Task<DocumentSnapshot?> ->
			if (uidTask.isSuccessful) {
				val document = uidTask.result
				if (!document!!.exists()) {
					uidRef.set(authenticatedUser).addOnCompleteListener { userCreationTask: Task<Void?> ->
						if (userCreationTask.isSuccessful) {
							authenticatedUser.let {
								val uid: String = it.uid
								val email: String = it.email
								val displayName: String = it.displayName
								val isNew: Boolean? = it.isNew
								val isAuthenticated: Boolean = it.isAuthenticated
								val isCreated: Boolean? = true
								newUserMutableLiveData.value = UserModel(uid, email, displayName, isNew, isAuthenticated, isCreated)
							}
						} else {
							Log.d(AuthenticationRepositoryImpl::class.java.simpleName, "${userCreationTask.exception?.message}")
						}
					}
				} else {
					newUserMutableLiveData.setValue(authenticatedUser)
				}
			} else {
				Log.d(AuthenticationRepositoryImpl::class.java.simpleName, "${uidTask.exception?.message}")
			}
		}
		return newUserMutableLiveData
	}
}