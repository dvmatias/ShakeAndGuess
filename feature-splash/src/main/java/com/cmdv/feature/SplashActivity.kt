package com.cmdv.feature

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cmdv.core.base.BaseMVVMActivity
import com.cmdv.core.extensions.applyImmersiveFullScreenWithNavigationBar
import com.cmdv.feature.databinding.ActivitySplashBinding

class SplashActivity : BaseMVVMActivity<ActivitySplashBinding>() {

	private lateinit var viewModel: SplashActivityViewModel

	override fun getLayoutId(): Int = R.layout.activity_splash

	override fun getNavigationHostFragmentId(): Int? = R.id.nav_host_fragment

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider(this).get(SplashActivityViewModel::class.java)

		applyImmersiveFullScreenWithNavigationBar()

		viewModel.getDestination()
		viewModel.destinationId.observe(this, Observer { singleEvent ->
			singleEvent.getContentIfNotHandledOrNull()?.let {
				navController?.navigate(it)
			}
		})

	}

	override fun onSupportNavigateUp(): Boolean {
		return findNavController(R.id.nav_host_fragment).navigateUp()
	}

}