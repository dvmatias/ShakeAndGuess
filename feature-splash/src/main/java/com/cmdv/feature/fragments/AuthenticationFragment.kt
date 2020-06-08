package com.cmdv.feature.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cmdv.feature.R

class AuthenticationFragment : Fragment() {

	companion object {
		fun newInstance() = AuthenticationFragment()
	}


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_authentication, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
	}

}