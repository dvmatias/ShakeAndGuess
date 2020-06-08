package com.cmdv.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.cmdv.core.base.BaseMVVMActivity
import com.cmdv.core.extensions.applyFullScreen
import com.cmdv.core.extensions.applyImmersiveFullScreenWithNavigationBar
import com.cmdv.screen.databinding.ActivitySplashBinding
import com.cmdv.screen.fragments.SplashActivityViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : BaseMVVMActivity<ActivitySplashBinding, SplashActivityViewModel, SplashActivityViewModelFactory>() {

	private lateinit var firebaseAuth: FirebaseAuth

	private lateinit var authStateListener: FirebaseAuth.AuthStateListener

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

	}

	override fun onSupportNavigateUp(): Boolean {
		return findNavController(R.id.nav_host_fragment).navigateUp()
	}

}