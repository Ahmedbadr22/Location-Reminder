package com.example.location.reminder.authentication.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.location.reminder.app.utils.ResponseResult
import com.example.location.reminder.authentication.data.model.LoginFormData
import com.example.location.reminder.authentication.data.model.RegistrationFormData
import com.example.location.reminder.authentication.data.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    val loginFormData = LoginFormData()
    val registrationFormData = RegistrationFormData()

    val loginState : LiveData<ResponseResult<Unit>?> = authRepository.loginState
    val loginGoogleAccountState : LiveData<ResponseResult<Unit>?> = authRepository.loginGoogleAccountState
    val registrationState : LiveData<ResponseResult<Unit>?> = authRepository.registrationState

    fun loginWithEmailAndPassword() {
        viewModelScope.launch {
            authRepository.loginWithEmailPassword(loginFormData)
        }
    }

    fun loginOrCreateGoogleAccount(authCredential: AuthCredential) {
        viewModelScope.launch {
            authRepository.loginOrCreateGoogleAccount(authCredential)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun createAccountWithEmailAndPassword() {
        viewModelScope.launch {
            authRepository.createAccountWithEmailPassword(registrationFormData)
        }
    }
}