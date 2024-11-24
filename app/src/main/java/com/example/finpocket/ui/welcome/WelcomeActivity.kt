package com.example.finpocket.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finpocket.R
import com.example.finpocket.ui.register.RegisterActivity
import com.google.android.material.button.MaterialButton

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val nextButton: MaterialButton = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            welcomeToRegister()
        }
    }

    private fun welcomeToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}