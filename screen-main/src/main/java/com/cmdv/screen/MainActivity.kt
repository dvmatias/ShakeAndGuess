package com.cmdv.screen

import android.os.Bundle
import com.cmdv.core.base.BaseMVVMActivity
import com.cmdv.screen.databinding.ActivityMainBinding

class MainActivity : BaseMVVMActivity<ActivityMainBinding, MainActivityViewModel, MainActivityViewModelFactory>() {

	override fun getLayoutId(): Int = R.layout.activity_main

	override fun getNavigationHostFragmentId(): Int? = R.id.nav_host_fragment_main

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}
}