package com.example.finpocket.ui.manualPlanning

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finpocket.MainActivity
import com.example.finpocket.R
import com.example.finpocket.adapter.CategoriesAdapter
import com.example.finpocket.api.ApiConfig
import com.example.finpocket.ui.recomendationPlanning.RecomendationPlanningActivity
import kotlinx.coroutines.launch

class ManualPlanningActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private val selectedCategories = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_planning)

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val categories = listOf(
            "Bills", "Groceries", "Transport", "Entertainment",
            "Healthcare", "Education", "Utilities"
        )

        categoriesAdapter = CategoriesAdapter(categories) { selected ->
            selectedCategories.clear()
            selectedCategories.addAll(selected)
            Log.d("ManualPlanningActivity", "Selected categories: $selectedCategories")
        }

        recyclerView.adapter = categoriesAdapter

        // Inisialisasi Tombol
        val aiRecommendationButton = findViewById<Button>(R.id.aiRecommendationButton)
        val manualRecommendationButton = findViewById<Button>(R.id.manualRecommendationButton)

        // Navigasi ke RecomendationPlanningActivity saat tombol AI Recommendation diklik
        aiRecommendationButton.setOnClickListener {
            val intent = Intent(this, RecomendationPlanningActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke MainActivity saat tombol Manual Recommendation diklik
        manualRecommendationButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
