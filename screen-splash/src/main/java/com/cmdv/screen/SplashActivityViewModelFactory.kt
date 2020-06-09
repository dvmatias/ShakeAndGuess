package com.cmdv.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmdv.data.repository.SplashRepositoryImpl
import com.cmdv.domain.repository.SplashRepository

class SplashActivityViewModelFactory(private val splashRepository: SplashRepository) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T =
		SplashActivityViewModel(splashRepository) as T

}