package com.example.finpocket.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finpocket.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelButton.setOnClickListener {
            navigateToProfileFragment()
        }

        // Save button logic
        binding.saveButton.setOnClickListener {
            savePassword()
        }
    }

    private fun navigateToProfileFragment() {
        finish()
    }

    private fun savePassword() {
        Toast.makeText(this, "Password berhasil diperbaharui", Toast.LENGTH_SHORT).show()
        navigateToProfileFragment() // Return to ProfileFragment after saving
    }
}