package com.example.securenotesapp

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SecureNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) //  Initialize Firebase in a Application class
        signInAnonymously()
        EncryptionHelper // initialize the EncryptionHelper class
    }

    /**
     * sign in using Firebase and sign in method is Anonymously()
     */
    private fun signInAnonymously() {
        val auth = FirebaseAuth.getInstance()
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.i("SecureNotesApp","Sign in success")
                } else {
                    // If sign in fails, display a message to the user.
                    val exception = task.exception
                    if (exception != null) {
                        Log.e("SecureNotesApp", "signInAnonymously: failure", exception)
                    } else {
                        Log.e("SecureNotesApp", "signInAnonymously: unknown failure")
                    }
                }
            }
    }
}