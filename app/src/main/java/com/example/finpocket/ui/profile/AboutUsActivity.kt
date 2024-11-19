package com.example.finpocket.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finpocket.R
import com.example.finpocket.databinding.ActivityAboutUsBinding

class AboutUsActivity: AppCompatActivity() {

    private lateinit var binding : ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            navigateToProfileFragment()
        }
    }

    private fun navigateToProfileFragment() {
        finish()
    }
}