package com.cmdv.domain.repository

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.model.CategoryModel

interface CategoryRepository {

	fun fetchCategories(): MutableLiveData<List<CategoryModel>>

}