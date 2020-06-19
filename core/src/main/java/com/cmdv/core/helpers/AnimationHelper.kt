package com.cmdv.core.helpers

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.cmdv.core.SimpleAnimationListener

class AnimationHelper(private val context: Context) {

    var anim: Animation? = null

    fun startAnimation(animResId: Int, v: View) {
        anim = AnimationUtils.loadAnimation(context, animResId)
        v.startAnimation(anim)
    }

    fun startAnimation(animResId: Int, v: View, onAnimEnd: () -> Unit) {
        anim = AnimationUtils.loadAnimation(context, animResId)
        anim?.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(anim: Animation?) {
                onAnimEnd.invoke()
            }
        })
        v.startAnimation(anim)
    }
}