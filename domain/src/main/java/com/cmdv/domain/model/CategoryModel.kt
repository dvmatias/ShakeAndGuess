package com.cmdv.domain.model

data class CategoryModel(
	val name: String,
	val imageName: String,
	val colorTop: String,
	val colorBottom: String,
	val items: List<CategoryItemModel>
) {

	data class CategoryItemModel(
		val name: String,
		val value: String
	)

}