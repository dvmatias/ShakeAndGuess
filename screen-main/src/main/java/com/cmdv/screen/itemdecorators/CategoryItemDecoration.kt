package com.cmdv.screen.itemdecorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.helpers.DisplayHelper

private const val OFFSET_TOP: Float = 16F
private const val OFFSET_HORIZONTAL: Float = 20F
private const val OFFSET_LAST_ITEM_BOTTOM: Float = 100F

class CategoryItemDecoration : RecyclerView.ItemDecoration() {

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		if (parent.layoutManager == null || parent.adapter == null) {
			return
		}

		val itemCount = parent.adapter!!.itemCount
		val childPosition = parent.getChildAdapterPosition(view)

		// All items - tp margin
		outRect.top = DisplayHelper.dpToPx(parent.context, OFFSET_TOP).toInt()
		outRect.left = DisplayHelper.dpToPx(parent.context, OFFSET_HORIZONTAL).toInt()
		// las row items
		if (isFourthItemInTheRow(childPosition)) {
			outRect.right = DisplayHelper.dpToPx(parent.context, OFFSET_HORIZONTAL).toInt()
		}
		// Last item
		if (childPosition + 1 == itemCount) {
			outRect.bottom = DisplayHelper.dpToPx(parent.context, OFFSET_LAST_ITEM_BOTTOM).toInt()
		}

	}

	private fun isFourthItemInTheRow(childPosition: Int): Boolean {
		val actualPosition = childPosition + 1
		return (actualPosition % 4) == 0
	}

}