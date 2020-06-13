package com.cmdv.core

import android.content.Context
import android.widget.Toast

fun showShortToast(context: Context, message: String) {
	Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
