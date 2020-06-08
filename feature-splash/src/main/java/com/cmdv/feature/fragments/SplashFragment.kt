package com.cmdv.feature.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmdv.feature.R
import com.cmdv.feature.SplashActivityViewModel

class SplashFragment : Fragment() {

	companion object {
		fun newInstance() = SplashFragment()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.splash_fragment, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
	}

}