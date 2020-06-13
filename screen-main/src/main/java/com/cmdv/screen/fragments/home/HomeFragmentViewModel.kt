package com.cmdv.screen.fragments.home

import androidx.lifecycle.MutableLiveData
import com.cmdv.core.base.BaseMVVMViewModel
import com.cmdv.domain.model.CategoryModel
import com.cmdv.domain.repository.CategoryRepository

internal class HomeFragmentViewModel(
	private val categoryRepository: CategoryRepository
) : BaseMVVMViewModel() {
	var categoriesLiveData = MutableLiveData<List<CategoryModel>>()

	fun fetchCategories() {
		categoriesLiveData = categoryRepository.fetchCategories()
	}

}