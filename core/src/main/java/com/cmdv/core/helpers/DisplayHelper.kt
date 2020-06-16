package com.cmdv.core.helpers

import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue
import kotlin.math.roundToInt

class DisplayHelper {

	@Suppress("unused")
	companion object {

		fun getStatusBarHeight(context: Context): Int {
			val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
			return if (resId > 0) {
				context.resources.getDimensionPixelSize(resId)
			} else {
				0
			}
		}

		fun getNavigationBarHeight(context: Context): Int {
			val resId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
			return if (resId > 0) {
				context.resources.getDimensionPixelSize(resId)
			} else {
				0
			}
		}

		fun getActionBarHeight(context: Context): Int {
			val styledAttributes: TypedArray = context.theme.obtainStyledAttributes(
				intArrayOf(android.R.attr.actionBarSize)
			)
			val actionBarHeight = styledAttributes.getDimension(0, 0F).toInt()
			styledAttributes.recycle()
			return actionBarHeight
		}

		fun dpToPx(context: Context, dp: Float): Float =
			TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)


		fun pxToDp(context: Context, px: Int): Int {
			val r = context.resources
			return (px / (r.displayMetrics.densityDpi / 160f)).roundToInt()
		}

	}

}