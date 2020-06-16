package com.cmdv.screen

import androidx.lifecycle.MutableLiveData
import com.cmdv.core.base.BaseMVVMViewModel

class MainActivityViewModel: BaseMVVMViewModel() {

	var displayCutoutLeft = MutableLiveData<Int>()

}