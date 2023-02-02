package com.example.location.reminder.authentication.data.source.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthDataSource {
    private val  auth : FirebaseAuth = Firebase.auth


    fun loginWithEmailPassword(email: String, password: String) : Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun createAccountWithEmailPassword(email: String, password: String) : Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun loginOrCreateGoogleAccount(authCredential: AuthCredential) : Task<AuthResult> {
        return auth.signInWithCredential(authCredential)
    }

    fun signOut() {
        auth.signOut()
    }
}