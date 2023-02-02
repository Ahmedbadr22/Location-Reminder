package com.example.location.reminder.authentication.data.repository

import androidx.lifecycle.LiveData
import com.example.location.reminder.app.utils.ResponseResult
import com.example.location.reminder.authentication.data.model.LoginFormData
import com.example.location.reminder.authentication.data.model.RegistrationFormData
import com.google.firebase.auth.AuthCredential

interface AuthRepository {
    val loginState: LiveData<ResponseResult<Unit>?>
    val registrationState: LiveData<ResponseResult<Unit>?>
    val loginGoogleAccountState: LiveData<ResponseResult<Unit>?>

    suspend fun loginWithEmailPassword(loginFormData: LoginFormData)
    suspend fun createAccountWithEmailPassword(registrationFormData: RegistrationFormData)

    suspend fun loginOrCreateGoogleAccount(authCredential: AuthCredential)
    fun signOut()
}