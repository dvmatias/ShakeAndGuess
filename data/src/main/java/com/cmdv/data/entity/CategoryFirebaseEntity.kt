package com.cmdv.data.entity

data class CategoryFirebaseEntity(
	val name: String?,
	val image: String?,
	var items: List<CategoryItemFirebaseEntity>?
) {

	constructor() : this(null, null, null)

}

data class CategoryItemFirebaseEntity(
	val name: String?,
	val value: String?
) {

	constructor() : this(null, null)

}