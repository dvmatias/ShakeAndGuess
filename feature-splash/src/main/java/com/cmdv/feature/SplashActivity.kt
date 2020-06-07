package com.cmdv.feature

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cmdv.core.base.BaseMVVMActivity
import com.cmdv.feature.databinding.ActivitySplashBinding

class SplashActivity : BaseMVVMActivity<ActivitySplashBinding>() {

	private lateinit var viewModel: SplashActivityViewModel

	override fun getLayoutId(): Int = R.layout.activity_splash

	override fun getNavigationHostFragmentId(): Int? = R.id.nav_host_fragment

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider(this).get(SplashActivityViewModel::class.java)

		applyImmersiveFullScreen()
		viewModel.getDestination()
		viewModel.destinationId.observe(this, Observer { singleEvent ->
			singleEvent.getContentIfNotHandledOrNull()?.let {
				navController?.navigate(it)
			}
		})
	}

	private fun applyImmersiveFullScreen() {
		hideSystemUI()
		window.decorView.setOnSystemUiVisibilityChangeListener {
			checkSystemUiVisibilityFullScreen(it)
		}
	}

	private fun hideSystemUI() {
		window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
				or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				or View.SYSTEM_UI_FLAG_FULLSCREEN)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
		}
	}

	private fun checkSystemUiVisibilityFullScreen(visibility: Int) {
		if (visibility == View.VISIBLE)
			Handler().postDelayed(
				{
					applyImmersiveFullScreen()
				}, 0
			)
	}

	override fun onSupportNavigateUp(): Boolean {
		return findNavController(R.id.nav_host_fragment).navigateUp()
	}

}