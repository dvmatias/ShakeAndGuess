package com.cmdv.core

import android.view.animation.Animation

abstract class SimpleAnimationListener : Animation.AnimationListener {

	override fun onAnimationRepeat(p0: Animation?) {}

	override fun onAnimationEnd(p0: Animation?) {}

	override fun onAnimationStart(p0: Animation?) {}

}