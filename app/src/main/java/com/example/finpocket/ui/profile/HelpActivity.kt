package com.example.finpocket.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finpocket.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            navigateToProfileFragment()
        }

        // Open WhatsApp
        binding.whatsapp.setOnClickListener {
            openWhatsApp()
        }
    }

    private fun navigateToProfileFragment() {
        finish()
    }

    private fun openWhatsApp() {
        try {
            val uri = Uri.parse("https://wa.me/6282116848487")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.whatsapp")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp tidak ditemukan di perangkat Anda", Toast.LENGTH_SHORT).show()
        }
    }
}
