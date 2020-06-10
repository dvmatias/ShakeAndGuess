package com.cmdv.core.extensions

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.WindowManager

fun Activity.applyImmersiveFullScreen() {
	clearWindowSystemUiVisibilityChangeListener()
	hideSystemUI(this)
	window.decorView.setOnSystemUiVisibilityChangeListener {
		checkSystemUiVisibilityFullScreen(it, this)
	}
}


private fun Activity.clearWindowSystemUiVisibilityChangeListener() {
	window.decorView.setOnSystemUiVisibilityChangeListener { }
}

private fun hideSystemUI(activity: Activity) {
	activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
			or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			or View.SYSTEM_UI_FLAG_FULLSCREEN)

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
		activity.window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
	}
}

private fun checkSystemUiVisibilityFullScreen(visibility: Int, activity: Activity) {
	if (visibility == View.VISIBLE)
		Handler().postDelayed(
			{
				activity.applyImmersiveFullScreen()
			}, 0
		)
}