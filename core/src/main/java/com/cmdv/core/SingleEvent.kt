package com.cmdv.core

open class SingleEvent<out T>(private val content: T) {

	var contentHasBeenHandled: Boolean = false
		private set

	/**
	 * Returns the content and prevents its use again.
	 */
	fun getContentIfNotHandledOrNull(): T? =
		when (contentHasBeenHandled) {
			true -> null
			false -> {
				contentHasBeenHandled = true
				content
			}
		}

	/**
	 * Returns the content, even if it's already been handled.
	 */
	fun peekContent(): T = content

}