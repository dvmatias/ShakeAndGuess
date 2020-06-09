package com.cmdv.screen.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmdv.core.Constants
import com.cmdv.data.repository.AuthenticationRepositoryImpl
import com.cmdv.domain.model.UserModel
import com.cmdv.screen.R
import com.cmdv.screen.SplashActivityViewModel
import com.cmdv.screen.databinding.FragmentAuthenticationBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class AuthenticationFragment : Fragment() {

	private lateinit var binding: FragmentAuthenticationBinding

	private lateinit var viewModel: AuthenticationFragmentViewModel

	private lateinit var googleSignInClient: GoogleSignInClient

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_authentication, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupViewModel()
		setupGoogleSignInButton()
		setupGoogleSignInClient()
	}

	private fun setupViewModel() {
		viewModel = ViewModelProvider(this, AuthenticationFragmentViewModelFactory(AuthenticationRepositoryImpl()))
			.get(AuthenticationFragmentViewModel::class.java)
	}

	private fun setupGoogleSignInButton() {
		binding.googleSignInButton.setOnClickListener {
			val signInIntent = googleSignInClient.signInIntent

			activity?.let {
				it.startActivityForResult(signInIntent, Constants.RC_GOOGLE_SIGN_IN)
				ViewModelProvider(it).get(SplashActivityViewModel::class.java)
					.googleSinInTask.observe(viewLifecycleOwner, Observer { task ->
						onGoogleSignInResult(task)
					})

			}
		}
	}

	private fun setupGoogleSignInClient() {
		val googleSignInOptions = GoogleSignInOptions
			.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(activity?.getString(R.string.default_web_client_id))
			.requestEmail()
			.build()

		activity?.let {
			googleSignInClient = GoogleSignIn.getClient(it, googleSignInOptions)
			googleSignInClient.signOut()
		}
	}

	private fun onGoogleSignInResult(task: Task<GoogleSignInAccount>) {
		try {
			val googleSignInAccount = task.getResult(ApiException::class.java)
			googleSignInAccount?.let {
				getGoogleAuthCredential(it)
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}

	}

	private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
		val googleTokenId = googleSignInAccount.idToken
		val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
		signInWithGoogleAuthCredential(googleAuthCredential)
	}

	private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
		viewModel.signInWithGoogle(googleAuthCredential)
		viewModel.authenticatedUserLiveData.observe(viewLifecycleOwner, Observer { authenticatedUser ->
			goToMainActivity(authenticatedUser)
		})
	}

	private fun goToMainActivity(authenticatedUser: UserModel) {
		Toast.makeText(activity, "Go To Main Activity!!", Toast.LENGTH_SHORT).show()
	}


}