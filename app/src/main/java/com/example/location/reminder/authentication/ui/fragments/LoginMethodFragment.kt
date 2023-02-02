package com.example.location.reminder.authentication.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.location.reminder.R
import com.example.location.reminder.authentication.ui.viewModel.AuthViewModel
import com.example.location.reminder.databinding.FragmentLoginMethodBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LoginMethodFragment : Fragment() {
    private lateinit var binding: FragmentLoginMethodBinding
    private val authViewModel : AuthViewModel by activityViewModel()

    private val signInWithGoogleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            activityResult.data?.let { intent ->
                val googleSignAccount = GoogleSignIn.getSignedInAccountFromIntent(intent)
                val credential = GoogleAuthProvider.getCredential(googleSignAccount.result.idToken, null)
                authViewModel.loginOrCreateGoogleAccount(credential)
                Log.d("LoginMethodFragment", "=> Not Null")
            }

            Log.d("LoginMethodFragment", " => Null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginMethodBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            fragment = this@LoginMethodFragment

            btnSignInWithGoogle.setOnClickListener {
                loginWithGoogleAccount()
            }
        }
    }

    private fun loginWithGoogleAccount() {
        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.web_client_api))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        val signInIntent = googleSignInClient.signInIntent
        signInWithGoogleLauncher.launch(signInIntent)
        Log.d("LoginMethodFragment", " => Intent Sent")
    }

    fun navigateToLoginFragment() {
        val navDirection = LoginMethodFragmentDirections.actionLoginMethodFragmentToLoginFragment()
        findNavController().navigate(navDirection)
    }
}