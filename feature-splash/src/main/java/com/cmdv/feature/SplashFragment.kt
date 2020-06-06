package com.cmdv.feature

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SplashFragment : Fragment() {

	companion object {
		fun newInstance() = SplashFragment()
	}

	private lateinit var viewModel: SplashFragmentViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.splash_fragment, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(this).get(SplashFragmentViewModel::class.java)
		// TODO: Use the ViewModel
	}

}