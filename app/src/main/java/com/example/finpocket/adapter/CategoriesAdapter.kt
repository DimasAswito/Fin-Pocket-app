package com.example.finpocket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finpocket.R

class CategoriesAdapter(
    private val categories: List<String>,
    private val onCategorySelected: (List<String>) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private val selectedCategories = mutableListOf<String>()

    // Peta ikon untuk setiap kategori
    private val categoryIcons = mapOf(
        "Bills" to R.drawable.bills,
        "Groceries" to R.drawable.groceries,
        "Transport" to R.drawable.transport,
        "Entertainment" to R.drawable.entertainment,
        "Healthcare" to R.drawable.healthcare,
        "Education" to R.drawable.education,
        "Utilities" to R.drawable.utilities,
        "Savings" to R.drawable.savings
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.categoryName.text = category

        // Set ikon sesuai kategori
        holder.categoryIcon.setImageResource(categoryIcons[category] ?: R.drawable.ic_baseline_wallet_24)

        // Logika untuk pemilihan kategori
        holder.itemView.setOnClickListener {
            if (selectedCategories.contains(category)) {
                selectedCategories.remove(category)
                holder.itemView.setBackgroundResource(R.drawable.category_unselected_background)
            } else {
                selectedCategories.add(category)
                holder.itemView.setBackgroundResource(R.drawable.category_selected_background)
            }
            onCategorySelected(selectedCategories)
        }
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.category_name)
        val categoryIcon: ImageView = view.findViewById(R.id.categoryIcon)
    }
}
