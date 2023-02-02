package com.example.location.reminder.authentication.data.repository

import androidx.lifecycle.LiveData
import com.example.location.reminder.app.utils.ResponseResult
import com.example.location.reminder.app.utils.SingleLiveEvent
import com.example.location.reminder.authentication.data.model.LoginFormData
import com.example.location.reminder.authentication.data.model.RegistrationFormData
import com.example.location.reminder.authentication.data.source.firebase.AuthDataSource
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    private val _loginState: SingleLiveEvent<ResponseResult<Unit>?> = SingleLiveEvent()
    override val loginState: LiveData<ResponseResult<Unit>?>
        get() = _loginState

    private val _loginGoogleAccountState: SingleLiveEvent<ResponseResult<Unit>?> = SingleLiveEvent()
    override val loginGoogleAccountState: LiveData<ResponseResult<Unit>?>
        get() = _loginGoogleAccountState

    private val _registrationState: SingleLiveEvent<ResponseResult<Unit>?> = SingleLiveEvent()
    override val registrationState: LiveData<ResponseResult<Unit>?>
        get() = _registrationState

    override suspend fun loginWithEmailPassword(loginFormData: LoginFormData) {
        _loginState.value = ResponseResult.Loading()
        _loginState.value = withContext(Dispatchers.IO) {
            try {
                authDataSource.loginWithEmailPassword(loginFormData.email, loginFormData.password).await()
                ResponseResult.Success(Unit)
            } catch (exception : Exception) {
                ResponseResult.Error(exception)
            }
        }
    }

    override suspend fun createAccountWithEmailPassword(registrationFormData: RegistrationFormData) {
        _registrationState.value = ResponseResult.Loading()
        _registrationState.value = withContext(Dispatchers.IO) {
            try {
                authDataSource.createAccountWithEmailPassword(registrationFormData.email, registrationFormData.password).await()
                ResponseResult.Success(Unit)
            } catch (exception : Exception) {
                ResponseResult.Error(exception)
            }
        }
    }

    override suspend fun loginOrCreateGoogleAccount(authCredential: AuthCredential) {
        _loginGoogleAccountState.value = ResponseResult.Loading()
        _loginGoogleAccountState.value = withContext(Dispatchers.IO) {
            try {
                authDataSource.loginOrCreateGoogleAccount(authCredential).await()
                ResponseResult.Success(Unit)
            } catch (exception : Exception) {
                ResponseResult.Error(exception)
            }
        }
    }

    override fun signOut() {
        authDataSource.signOut()
    }
}