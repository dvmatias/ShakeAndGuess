package com.cmdv.core

import androidx.lifecycle.MutableLiveData

interface GuessSensor {

    fun registerListener()

    fun unregisterListener()

    fun updateGetGuessResultOnlyIfItChanges(): MutableLiveData<Int>

}