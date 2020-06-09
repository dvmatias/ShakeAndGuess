package com.cmdv.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmdv.core.Constants
import com.cmdv.core.base.BaseMVVMActivity
import com.cmdv.core.extensions.applyFullScreen
import com.cmdv.core.extensions.applyImmersiveFullScreenWithNavigationBar
import com.cmdv.data.repository.SplashRepositoryImpl
import com.cmdv.screen.databinding.ActivitySplashBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn


class SplashActivity : BaseMVVMActivity<ActivitySplashBinding, SplashActivityViewModel, SplashActivityViewModelFactory>() {

	private lateinit var viewModel: SplashActivityViewModel

	override fun getLayoutId(): Int = R.layout.activity_splash

	override fun getNavigationHostFragmentId(): Int? = R.id.nav_host_fragment

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		applyImmersiveFullScreenWithNavigationBar()

		setupViewModel()
		checkIfUserIsAuthenticated()
	}

	private fun setupViewModel() {
		viewModel = ViewModelProvider(this, SplashActivityViewModelFactory(SplashRepositoryImpl())).get(SplashActivityViewModel::class.java)
	}

	private fun checkIfUserIsAuthenticated() {
		viewModel.checkIfUserIsAuthenticated()
		viewModel.isUserAuthenticatedLiveData.observe(this, Observer { user ->
			if (user.isAuthenticated) {
				viewModel.userLiveData.observe(this, Observer {
					Log.d(SplashActivity::class.java.simpleName, "Go to Main Activity! - User $user")
					// TODO goToMainActivity(it)
				})
				getUserFromDataBase(user.uid)
				finish()
			} else {
				goToAuthenticationFragment()
			}
		})
	}

	private fun goToAuthenticationFragment() {
		navController?.navigate(R.id.action_splashFragment_to_authenticationFragment)
		applyFullScreen()
	}

	private fun getUserFromDataBase(uid: String) {
		viewModel.setUid(uid)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (Constants.RC_GOOGLE_SIGN_IN == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				Toast.makeText(this, "Google Sign In -> Result OK!   :) ", Toast.LENGTH_SHORT).show()
				val task = GoogleSignIn.getSignedInAccountFromIntent(data)
				viewModel.googleSinInTask.postValue(task)
			}
		}

	}
}