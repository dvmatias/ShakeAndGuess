package com.cmdv.screen

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import com.cmdv.core.base.BaseMVVMActivity
import com.cmdv.core.extensions.applyImmersiveFullScreen
import com.cmdv.screen.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : BaseMVVMActivity<ActivityMainBinding, MainActivityViewModel, MainActivityViewModelFactory>() {

	private lateinit var viewModel: MainActivityViewModel

	override fun getLayoutId(): Int = R.layout.activity_main

	override fun getNavigationHostFragmentId(): Int? = R.id.nav_host_fragment_main

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		applyImmersiveFullScreen()

		setupViewModel()
		setupSignOutButton()
		binding.root.viewTreeObserver.addOnGlobalLayoutListener(onDisplayCutoutReadyListener)
	}

	private fun setupViewModel() {
		viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
	}

	private val onDisplayCutoutReadyListener: ViewTreeObserver.OnGlobalLayoutListener =
		ViewTreeObserver.OnGlobalLayoutListener {
			val leftMargin =
				if (SDK_INT >= Build.VERSION_CODES.P) window.decorView.rootWindowInsets.displayCutout?.safeInsetLeft ?: 0
				else 0
			viewModel.displayCutoutLeft.postValue(leftMargin)
		}

	private fun setupSignOutButton() {
//		binding.fabSignOut.setOnClickListener {
//			FirebaseAuth.getInstance().let { firebaseAuth ->
//				firebaseAuth.currentUser?.providerData?.let {
//					for (userInfo in it) {
//						when (userInfo.providerId) {
//							"google.com" -> googleSignOut()
//						}
//					}
//				}
//				firebaseAuth.signOut()
//			}
//			finish()
//		}
	}

	private fun googleSignOut() {
		val googleSignInOptions = GoogleSignInOptions
			.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()

		GoogleSignIn.getClient(this, googleSignInOptions).signOut()
	}

}