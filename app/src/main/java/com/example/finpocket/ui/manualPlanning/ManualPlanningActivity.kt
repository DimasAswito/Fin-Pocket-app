package com.example.finpocket.ui.manualPlanning

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finpocket.MainActivity
import com.example.finpocket.R
import com.example.finpocket.adapter.CategoriesAdapter
import com.example.finpocket.api.ApiConfig
import com.example.finpocket.model.Budget
import com.example.finpocket.model.Plan
import com.example.finpocket.model.PlanRequest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

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
            "Healthcare", "Education", "Utilities", "Savings"
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
            handleAiRecommendation()
        }

        // Navigasi ke MainActivity saat tombol Manual Recommendation diklik
        manualRecommendationButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateBudgets(): List<Budget> {
        return listOf(
            Budget(category_id = 1, percentage = 0.4, amount = 1250000),
            Budget(category_id = 2, percentage = 0.2, amount = 450000),
            Budget(category_id = 8, percentage = 0.4, amount = 300000)
        )
    }

    private fun handleAiRecommendation() {
        val income : CurrencyEditText = findViewById<CurrencyEditText>(R.id.budgetEditText)

        if (income == null) {
            Toast.makeText(this, "Please input a valid budget amount", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan income ke SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("income", income.getIntValue().toInt())
        editor.apply()

        // Navigasi ke HomeFragment
        val intent = Intent(this, MainActivity::class.java) // MainActivity sebagai container HomeFragment
        intent.putExtra("navigateTo", "HomeFragment") // Tambahkan informasi navigasi
        startActivity(intent)
    }


    private fun sendPlan(firebaseId: String, planRequest: PlanRequest) {
        lifecycleScope.launch {
            try {
                val response = ApiConfig.instance.postPlan("Bearer $firebaseId", planRequest)
                Log.d("ManualPlanningActivity", "Sending PlanRequest: $planRequest")
                if (response.isSuccessful) {
                    Toast.makeText(this@ManualPlanningActivity, "Plan submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(this@ManualPlanningActivity, "Failed to submit plan: $errorBody", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ManualPlanningActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

