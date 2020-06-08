package com.cmdv.screen.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SplashActivityViewModelFactory : ViewModelProvider.Factory {

	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return modelClass.getConstructor().newInstance()
	}

}