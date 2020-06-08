package com.cmdv.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.cmdv.core.Constants
import com.cmdv.core.base.BaseMVVMActivity
import com.cmdv.core.extensions.applyFullScreen
import com.cmdv.core.extensions.applyImmersiveFullScreenWithNavigationBar
import com.cmdv.screen.databinding.ActivitySplashBinding
import com.cmdv.screen.fragments.SplashActivityViewModelFactory
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo

class SplashActivity : BaseMVVMActivity<ActivitySplashBinding, SplashActivityViewModel, SplashActivityViewModelFactory>() {

	private lateinit var firebaseAuth: FirebaseAuth

	private var authStateListener: FirebaseAuth.AuthStateListener? = null

	override fun getLayoutId(): Int = R.layout.activity_splash

	override fun getNavigationHostFragmentId(): Int? = R.id.nav_host_fragment

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		applyImmersiveFullScreenWithNavigationBar()

		viewModel.getDestination()
		viewModel.destinationId.observe(this, Observer { singleEvent ->
			singleEvent.getContentIfNotHandledOrNull()?.let {
				navController?.navigate(it)
				applyFullScreen()
			}
		})

		firebaseAuth = FirebaseAuth.getInstance()
		authStateListener = FirebaseAuth.AuthStateListener {
			// This block will execute when user's authentication status changes.
			val user: FirebaseUser? = firebaseAuth.currentUser
			if (user != null) {
				// TODO go to main activity
				AuthUI.getInstance().signOut(this)
			} else {
				// Init authentication providers
				startActivityForResult(
					AuthUI.getInstance()
						.createSignInIntentBuilder()
						.setIsSmartLockEnabled(false)
						.setAvailableProviders(
							listOf(
								AuthUI.IdpConfig.EmailBuilder().build(),
								AuthUI.IdpConfig.GoogleBuilder().build()
							)
						)
						.setLogo(R.drawable.img_login_logo)
						.setTheme(R.style.LoginTheme)
						.build(),
					Constants.RC_SIGN_IN
				)
			}
		}

	}

	override fun onSupportNavigateUp(): Boolean {
		return findNavController(R.id.nav_host_fragment).navigateUp()
	}

	override fun onResume() {
		super.onResume()
		// Bind Firebase Auth instance whit Firebase Auth State Listener.
		authStateListener?.let {
			firebaseAuth.addAuthStateListener(it)
		}
	}

	override fun onPause() {
		super.onPause()
		// Remove Firebase Auth State Listener from Firebase Auth instance.
		authStateListener?.let {
			firebaseAuth.removeAuthStateListener(it)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (Constants.RC_SIGN_IN == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				Toast.makeText(this, "Sign In -> Result OK!   :) ", Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(this, "Sign In -> Result KO!   :( ", Toast.LENGTH_SHORT).show()
			}
		}
	}

	

}