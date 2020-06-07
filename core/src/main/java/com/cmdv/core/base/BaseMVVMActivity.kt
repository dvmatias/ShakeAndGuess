package com.cmdv.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.Navigation

@Suppress("UNCHECKED_CAST")
abstract class BaseMVVMActivity<B : ViewDataBinding> : AppCompatActivity() {

	protected lateinit var binding: B

	protected var navController: NavController? = null

	protected abstract fun getLayoutId(): Int

	protected abstract fun getNavigationHostFragmentId(): Int?

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, getLayoutId())
		getNavigationHostFragmentId()?.let { navController = Navigation.findNavController(this, it) }
	}
}