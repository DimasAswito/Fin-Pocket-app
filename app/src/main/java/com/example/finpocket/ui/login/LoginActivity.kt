package com.example.finpocket.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.lifecycleScope
import com.example.finpocket.MainActivity
import com.example.finpocket.R
import com.example.finpocket.databinding.ActivityLoginBinding
import com.example.finpocket.ui.register.RegisterActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Setup tombol login Google
        setupGoogleLogin()

        // Navigasi ke RegisterActivity jika tidak punya akun
        binding.loginLink.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun setupGoogleLogin() {
        binding.googleLoginButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val credentialManager = CredentialManager.create(this)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id))
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse =
                    credentialManager.getCredential(this@LoginActivity, request)
                val idToken = (result.credential as? GoogleIdTokenCredential)?.idToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Google login failed: ${e.message}")
                Toast.makeText(
                    this@LoginActivity,
                    "Failed to login with Google. Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    checkUserRegistration(user.email ?: "")
                }
            } else {
                Log.e(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUserRegistration(email: String) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val signInMethods = task.result?.signInMethods
                if (signInMethods.isNullOrEmpty()) {
                    // Akun tidak terdaftar
                    showNotRegisteredDialog()
                } else {
                    // Akun terdaftar
                    navigateToMainActivity()
                }
            } else {
                Log.e(TAG, "Error checking user registration", task.exception)
                Toast.makeText(
                    this,
                    "Error occurred while checking registration.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showNotRegisteredDialog() {
        Toast.makeText(this, "This Account Not Registered", Toast.LENGTH_LONG).show()
        // Tombol register (navigasi ke RegisterActivity)
        binding.loginLink.performClick()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
