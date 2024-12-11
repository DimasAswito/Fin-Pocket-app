package com.example.finpocket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finpocket.R
import com.example.finpocket.model.HistoryItem

class HistoryAdapter(private val originalItems: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items = originalItems.toList()

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.itemIcon)
        val name: TextView = view.findViewById(R.id.itemName)
        val category: TextView = view.findViewById(R.id.itemCategory)
        val amount: TextView = view.findViewById(R.id.itemAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.icon)
        holder.name.text = item.name
        holder.category.text = item.category
        holder.amount.text = "Rp.${item.amount}"

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(item)
        }
    }

    override fun getItemCount() = items.size

    fun filterByCategory(category: String?) {
        items = if (category.isNullOrEmpty() || category == "Choose Category") {
            originalItems
        } else {
            originalItems.filter { it.category == category }
        }
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (HistoryItem) -> Unit) {
        itemClickListener = listener
    }

    private var itemClickListener: ((HistoryItem) -> Unit)? = null
}
