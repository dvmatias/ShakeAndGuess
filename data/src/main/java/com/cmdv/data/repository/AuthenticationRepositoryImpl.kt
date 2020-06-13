package com.cmdv.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.model.UserModel
import com.cmdv.domain.repository.AuthenticationRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class AuthenticationRepositoryImpl : AuthenticationRepository {

	private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

	private val databaseRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

	private val usersDatabaseRef: DatabaseReference = databaseRootRef.getReference("users")

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
					val isNew: Boolean = isNewUser
					val isAuthenticated = true
					val isCreated = false
					authenticatedUserMutableLiveData.postValue(UserModel(uid, email, displayName, isNew, isAuthenticated, isCreated))
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

	val listener = DatabaseReference.CompletionListener { p0, p1 ->
		p1.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onCancelled(p0: DatabaseError) {
				TODO("Not yet implemented")
			}

			override fun onDataChange(p0: DataSnapshot) {
				TODO("Not yet implemented")
			}
		})
	}

	override fun createUserInFirebaseDatabaseIfNotExists(authenticatedUser: UserModel): MutableLiveData<UserModel> {
		val newUserMutableLiveData = MutableLiveData<UserModel>()

		usersDatabaseRef.child(authenticatedUser.uid).setValue(authenticatedUser, DatabaseReference.CompletionListener { p0, p1 ->
			val uidRef = p1.setValue(authenticatedUser)
			uidRef.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					authenticatedUser.let {
						val uid: String = it.uid
						val email: String = it.email
						val displayName: String = it.displayName
						val isNew: Boolean = it.isNew
						val isAuthenticated: Boolean = it.isAuthenticated
						val isCreated = true
						newUserMutableLiveData.postValue(UserModel(uid, email, displayName, isNew, isAuthenticated, isCreated))
					}
				} else {
					Log.d(AuthenticationRepositoryImpl::class.java.simpleName, "${task.exception?.message}")
				}
			}
		})

		/*

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
								val isNew: Boolean = it.isNew
								val isAuthenticated: Boolean = it.isAuthenticated
								val isCreated = true
								newUserMutableLiveData.postValue(UserModel(uid, email, displayName, isNew, isAuthenticated, isCreated))
							}
						} else {
							Log.d(AuthenticationRepositoryImpl::class.java.simpleName, "${userCreationTask.exception?.message}")
						}
					}
				} else {
					newUserMutableLiveData.postValue(authenticatedUser)
				}
			} else {
				Log.d(AuthenticationRepositoryImpl::class.java.simpleName, "${uidTask.exception?.message}")
			}
		}

		*/
		return newUserMutableLiveData
	}
}