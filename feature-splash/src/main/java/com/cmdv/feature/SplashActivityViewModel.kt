package com.cmdv.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.core.base.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivityViewModel : ViewModel() {
	var destinationId = MutableLiveData<SingleEvent<Int?>>()

	fun getDestination() {
		GlobalScope.launch(Dispatchers.IO) {
			Thread.sleep(5000)

			GlobalScope.launch(Dispatchers.Main) {
				destinationId.value =
				when (checkIfUserIsLoggedIn()) {
					true -> SingleEvent(null)
					false -> SingleEvent(R.id.action_splashFragment_to_authenticationFragment)
				}
			}
		}
	}

	private fun checkIfUserIsLoggedIn(): Boolean {
		return false
	}

}