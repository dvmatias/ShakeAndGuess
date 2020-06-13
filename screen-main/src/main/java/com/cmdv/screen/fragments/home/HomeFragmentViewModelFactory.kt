package com.cmdv.screen.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmdv.domain.repository.CategoryRepository

internal class HomeFragmentViewModelFactory(
	private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T =
		HomeFragmentViewModel(categoryRepository) as T

}