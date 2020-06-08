package com.cmdv.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseMVVMActivity<B : ViewDataBinding, VM : BaseMVVMViewModel, F : ViewModelProvider.Factory> : AppCompatActivity() {

	protected lateinit var viewModel: VM
		private set

	@Suppress("MemberVisibilityCanBePrivate")
	protected lateinit var binding: B
		private set

	protected var navController: NavController? = null
		private set

	protected abstract fun getLayoutId(): Int

	protected abstract fun getNavigationHostFragmentId(): Int?

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupViewModel()
		setupDataBinding()
		getNavigationHostFragmentId()?.let { navController = Navigation.findNavController(this, it) }
	}

	private fun setupViewModel() {
		val clazz: Class<VM> = getViewModelClass(javaClass)
		viewModel = ViewModelProvider(this, getFactoryClass(javaClass)).get(clazz)
	}

	private fun getViewModelClass(clazz: Class<*>): Class<VM> {
		val type = clazz.genericSuperclass

		return if (type is ParameterizedType) {
			type.actualTypeArguments[1] as Class<VM>
		} else {
			getViewModelClass(clazz.superclass as Class<*>)
		}
	}

	private fun getFactoryClass(clazz: Class<*>): ViewModelProvider.Factory =
		((clazz.genericSuperclass as ParameterizedType).actualTypeArguments[2] as Class<F>).newInstance()


	private fun setupDataBinding() {
		binding = DataBindingUtil.setContentView(this, getLayoutId())
	}

}