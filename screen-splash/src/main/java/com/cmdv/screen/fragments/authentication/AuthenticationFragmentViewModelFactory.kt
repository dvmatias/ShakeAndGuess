package com.cmdv.screen.fragments.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmdv.domain.repository.AuthenticationRepository

class AuthenticationFragmentViewModelFactory(private val authenticationRepository: AuthenticationRepository) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T =
		AuthenticationFragmentViewModel(authenticationRepository) as T
}