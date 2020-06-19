package com.cmdv.core

import android.view.animation.Animation

abstract class SimpleAnimationListener : Animation.AnimationListener {

	override fun onAnimationRepeat(anim: Animation?) {}
	override fun onAnimationEnd(anim: Animation?) {}
	override fun onAnimationStart(anim: Animation?) {}

}