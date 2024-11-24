package com.example.finpocket.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.finpocket.R
import com.example.finpocket.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

//        // Tombol Sign In
//        val signInButton: MaterialButton = findViewById(R.id.loginButton)
//        signInButton.setOnClickListener {
//            navigateToLogin()
//        }

        // Teks Login
        val loginText: TextView = findViewById(R.id.loginLink)
        loginText.setOnClickListener {
            navigateToLogin()
        }
    }

    // Fungsi untuk navigasi ke halaman Login
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
