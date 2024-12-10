package com.example.finpocket.ui.recomendationPlanning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finpocket.MainActivity
import com.example.finpocket.R
import com.example.finpocket.databinding.ActivityRecomendationPlanningBinding
import com.example.finpocket.databinding.ActivityRegisterBinding

class RecomendationPlanningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendationPlanningBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendation_planning)
        setContentView(binding.root)

        binding.AiRecommendation.setOnClickListener {
            AIRecommendation()
        }
    }

    private fun AIRecommendation() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}