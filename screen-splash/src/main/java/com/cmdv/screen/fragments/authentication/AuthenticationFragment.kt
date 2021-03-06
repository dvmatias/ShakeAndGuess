package com.cmdv.screen.fragments.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmdv.core.Constants
import com.cmdv.data.repository.AuthenticationRepositoryImpl
import com.cmdv.domain.model.UserModel
import com.cmdv.screen.R
import com.cmdv.screen.SplashActivity
import com.cmdv.screen.databinding.FragmentAuthenticationBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
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
			signInWithGoogle()
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
		}
	}

	private fun signInWithGoogle() {
		val signInIntent = googleSignInClient.signInIntent

		startActivityForResult(signInIntent, Constants.RC_GOOGLE_SIGN_IN)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (Constants.RC_GOOGLE_SIGN_IN == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				val task = GoogleSignIn.getSignedInAccountFromIntent(data)
				try {
					val googleSignInAccount: GoogleSignInAccount? = task.getResult(ApiException::class.java)
					googleSignInAccount?.let {
						getGoogleAuthCredential(it)
					}
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}
		}
	}

	private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
		val googleTokenId = googleSignInAccount.idToken
		val googleAuthCredential: AuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
		signInWithGoogleAuthCredential(googleAuthCredential)
	}

	private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
		viewModel.signInWithGoogle(googleAuthCredential)
		viewModel.authenticatedUserLiveData.observe(viewLifecycleOwner, Observer { authenticatedUser ->
			if (authenticatedUser.isNew) {
				createNewUser(authenticatedUser)
			} else {
				goToMainActivity(authenticatedUser)
			}
		})
	}

	private fun createNewUser(authenticatedUser: UserModel) {
		viewModel.createUser(authenticatedUser)
		viewModel.createdUserLiveData.observe(viewLifecycleOwner, Observer { user ->
			goToMainActivity(user)
		})
	}

	/*****************************************************************************************************************
	 * Destinations
	 */

	private fun goToMainActivity(authenticatedUser: UserModel) {
		(activity as SplashActivity).goToMainActivity(authenticatedUser)
	}

}