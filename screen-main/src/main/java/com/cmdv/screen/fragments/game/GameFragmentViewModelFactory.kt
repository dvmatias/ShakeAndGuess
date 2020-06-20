package com.cmdv.screen.fragments.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmdv.core.GuessSensor

class GameFragmentViewModelFactory(private val guessSensor: GuessSensor) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        GameFragmentViewModel(guessSensor) as T
}