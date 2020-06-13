package com.cmdv.data.repository

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.model.UserModel
import com.cmdv.domain.repository.SplashRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashRepositoryImpl : SplashRepository {

	private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

	private val databaseRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

	private val usersDatabaseRef: DatabaseReference = databaseRootRef.getReference("users")

	/**
	 *
	 */
	override fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<UserModel> {
		val isUserAuthenticateInFirebaseMutableLiveData = MutableLiveData<UserModel>()
		val firebaseUser = firebaseAuth.currentUser

		val uid: String = firebaseUser?.uid ?: ""
		val email: String = firebaseUser?.email ?: ""
		val displayName: String = firebaseUser?.displayName ?: ""
		val isNew = false
		val isAuthenticated: Boolean = firebaseUser != null
		val isCreated = false

		isUserAuthenticateInFirebaseMutableLiveData.postValue(UserModel(uid, email, displayName, isNew, isAuthenticated, isCreated))
		return isUserAuthenticateInFirebaseMutableLiveData
	}

	/**
	 *
	 */
	override fun addUserToLiveData(uid: String): MutableLiveData<UserModel> {
		val userMutableLiveData: MutableLiveData<UserModel> = MutableLiveData()

		usersDatabaseRef.push().setValue(uid)
		usersDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onCancelled(p0: DatabaseError) {
				TODO("Not yet implemented")
			}

			override fun onDataChange(p0: DataSnapshot) {
				userMutableLiveData.postValue(p0.getValue(UserModel::class.java))
			}
		})
		/*
		usersRef.document(uid).get().addOnCompleteListener{userTask ->
			if (userTask.isSuccessful) {
				val document: DocumentSnapshot? = userTask.result
				document?.let {
					if (it.exists()) {
						val user = it.toObject(UserModel::class.java)
						userMutableLiveData.postValue(user)
					}
				}
			} else {
				Log.d("SplashRepositoryImpl", "UserTask ERROR: ${userTask.exception?.message}")
				// TODO handle this error.
				firebaseAuth.signOut()
			}
		}
		*/

		return userMutableLiveData
	}
}