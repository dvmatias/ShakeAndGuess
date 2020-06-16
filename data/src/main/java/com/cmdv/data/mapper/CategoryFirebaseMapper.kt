package com.cmdv.data.mapper

import com.cmdv.data.entity.CategoryFirebaseEntity
import com.cmdv.data.entity.CategoryItemFirebaseEntity
import com.cmdv.domain.mapper.Mapper
import com.cmdv.domain.model.CategoryModel

private const val CATEGORY_NAME_DEFAULT = ""
private const val CATEGORY_IMAGE_URL_DEFAULT = ""
private const val CATEGORY_COLOR_TOP_DEFAULT = "#FFFFFF"
private const val CATEGORY_COLOR_BOTTOM_DEFAULT = "#EE497B"
private const val ITEM_NAME_DEFAULT = ""
private const val ITEM_VALUE_DEFAULT = ""

class CategoryFirebaseMapper : Mapper<CategoryFirebaseEntity, CategoryModel>() {

	override fun transformEntityToModel(e: CategoryFirebaseEntity): CategoryModel {
		val name: String = e.name ?: CATEGORY_NAME_DEFAULT
		val imageName: String = e.imageName ?: CATEGORY_IMAGE_URL_DEFAULT
		val colorTop: String = e.colorTop ?: CATEGORY_COLOR_TOP_DEFAULT
		val colorBottom: String = e.colorBottom ?: CATEGORY_COLOR_BOTTOM_DEFAULT
		val items: List<CategoryModel.CategoryItemModel> = transformItems(e.items)

		return CategoryModel(name, imageName, colorTop, colorBottom, items)
	}

	private fun transformItems(items: List<CategoryItemFirebaseEntity>?): List<CategoryModel.CategoryItemModel> =
		items?.map {
			CategoryModel.CategoryItemModel(
				name = it.name ?: ITEM_NAME_DEFAULT,
				value = it.value ?: ITEM_VALUE_DEFAULT
			)
		} ?: listOf()

}