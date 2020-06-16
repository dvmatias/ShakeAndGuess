package com.cmdv.data.entity

/**
 * Firebase Entity
 */
data class CategoryFirebaseEntity(
	val name: String?,
	val imageName: String?,
	val colorTop: String?,
	val colorBottom: String?,
	var items: List<CategoryItemFirebaseEntity>?
) {

	@Suppress("unused")
	constructor() : this(null, null, null, null, null)

}

/**
 * Firebase Entity
 */
data class CategoryItemFirebaseEntity(
	val name: String?,
	val value: String?
) {

	@Suppress("unused")
	constructor() : this(null, null)

}