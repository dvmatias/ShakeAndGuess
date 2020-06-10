package com.cmdv.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmdv.core.base.BaseMVVMActivity
import com.cmdv.core.extensions.applyImmersiveFullScreen
import com.cmdv.data.repository.SplashRepositoryImpl
import com.cmdv.domain.model.UserModel
import com.cmdv.screen.databinding.ActivitySplashBinding
import com.google.gson.Gson

class SplashActivity : BaseMVVMActivity<ActivitySplashBinding, SplashActivityViewModel, SplashActivityViewModelFactory>() {

	private lateinit var viewModel: SplashActivityViewModel

	override fun getLayoutId(): Int = R.layout.activity_splash

	override fun getNavigationHostFragmentId(): Int? = R.id.nav_host_fragment

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		applyImmersiveFullScreen()

		setupViewModel()
		checkIfUserIsAuthenticated()
	}

	override fun onBackPressed() {
		finish()
	}

	private fun setupViewModel() {
		viewModel = ViewModelProvider(this, SplashActivityViewModelFactory(SplashRepositoryImpl())).get(SplashActivityViewModel::class.java)
	}

	private fun checkIfUserIsAuthenticated() {
		viewModel.checkIfUserIsAuthenticated()
		viewModel.isUserAuthenticatedLiveData.observe(this, Observer { user ->
			if (user.isAuthenticated) {
				getUserFromDataBase(user.uid)
			} else {
				goToAuthenticationFragment()
			}
		})
	}

	private fun getUserFromDataBase(uid: String) {
		viewModel.setUid(uid)
		viewModel.userLiveData.observe(this, Observer { authenticatedUser ->
			goToMainActivity(authenticatedUser)
			finish()
		})
	}

	/*****************************************************************************************************************
	 * Destinations
	 */

	private fun goToAuthenticationFragment() {
		navController?.navigate(R.id.action_splashFragment_to_authenticationFragment)
	}

	fun goToMainActivity(authenticatedUser: UserModel) {
		val bundle = Bundle()
		bundle.putString("name", Gson().toJson(authenticatedUser))
		navController?.navigate(R.id.action_to_mainActivity, bundle)
		finish()
	}

}