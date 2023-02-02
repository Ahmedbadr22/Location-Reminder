package com.example.location.reminder.authentication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.location.reminder.app.utils.ResponseResult
import com.example.location.reminder.authentication.ui.viewModel.AuthViewModel
import com.example.location.reminder.databinding.FragmentRegistrationBinding
import com.example.location.reminder.reminders.RemindersActivity
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding

    private val authViewModel: AuthViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = authViewModel
        }

        authViewModel.registrationState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResponseResult.Loading -> showToast("Loading..")
                is ResponseResult.Success -> navigateToRemindersActivity()
                is ResponseResult.Error -> {
                    result.exception?.let { exception ->
                        exception.message?.let { message ->
                            showToast(message)
                        }
                    }

                }
                else -> Log.d("LoginFragment", "==> Null")
            }
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToRemindersActivity() {
        val intent = Intent(requireActivity(), RemindersActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}