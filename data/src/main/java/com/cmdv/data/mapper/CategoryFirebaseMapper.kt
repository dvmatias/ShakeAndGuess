package com.cmdv.data.mapper

import com.cmdv.data.entity.CategoryFirebaseEntity
import com.cmdv.data.entity.CategoryItemFirebaseEntity
import com.cmdv.domain.mapper.Mapper
import com.cmdv.domain.model.CategoryModel

private const val CATEGORY_NAME_DEFAULT = ""
private const val CATEGORY_IMAGE_DEFAULT = ""
private const val ITEM_NAME_DEFAULT = ""
private const val ITEM_VALUE_DEFAULT = ""

class CategoryFirebaseMapper : Mapper<CategoryFirebaseEntity, CategoryModel>() {

	override fun transformEntityToModel(e: CategoryFirebaseEntity): CategoryModel {
		val name: String = e.name ?: CATEGORY_NAME_DEFAULT
		val image: String = e.image ?: CATEGORY_IMAGE_DEFAULT
		val items: List<CategoryModel.CategoryItemModel> = transformItems(e.items)

		return CategoryModel(name, image, items)
	}

	private fun transformItems(items: List<CategoryItemFirebaseEntity>?): List<CategoryModel.CategoryItemModel> =
		items?.map {
			CategoryModel.CategoryItemModel(
				it.name ?: ITEM_NAME_DEFAULT,
				it.value ?: ITEM_VALUE_DEFAULT
			)
		} ?: listOf()

}