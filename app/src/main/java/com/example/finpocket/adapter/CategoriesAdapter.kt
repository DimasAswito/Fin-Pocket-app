package com.example.finpocket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.finpocket.R
import com.example.finpocket.model.Category

class CategoriesAdapter(
    private val categories: List<String>,
    private val onCategorySelected: (List<String>) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    // List untuk melacak kategori yang dipilih
    private val selectedCategories = mutableListOf<String>()

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.category_name)
        val container: CardView = view.findViewById(R.id.categoryContainer)

        fun bind(category: String) {
            categoryName.text = category

            // Ubah tampilan berdasarkan status seleksi
            if (selectedCategories.contains(category)) {
                container.setBackgroundResource(R.drawable.category_selected_background)
                categoryName.setTextColor(itemView.context.getColor(R.color.black_text))
            } else {
                container.setBackgroundResource(R.drawable.category_unselected_background)
                categoryName.setTextColor(itemView.context.getColor(R.color.black_text))
            }

            // Tambahkan logika klik
            itemView.setOnClickListener {
                if (selectedCategories.contains(category)) {
                    selectedCategories.remove(category)
                } else {
                    selectedCategories.add(category)
                }
                notifyItemChanged(adapterPosition)
                onCategorySelected(selectedCategories)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}

