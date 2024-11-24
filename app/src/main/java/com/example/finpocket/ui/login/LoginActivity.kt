package com.example.finpocket.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.finpocket.MainActivity
import com.example.finpocket.R
import com.example.finpocket.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Teks Sign Up
        val signUpText: TextView = findViewById(R.id.loginLink)
        signUpText.setOnClickListener {
            navigateToRegister()
        }
    }

    // Fungsi navigasi ke MainActivity
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Mengakhiri LoginActivity setelah berhasil login
    }

    // Fungsi navigasi ke RegisterActivity
    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
