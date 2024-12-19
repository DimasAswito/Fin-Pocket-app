package com.example.finpocket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finpocket.R
import com.example.finpocket.model.HistoryItem
import java.text.NumberFormat
import java.util.Locale

class HistoryAdapter(
    private var historyItems: List<HistoryItem>,
    private val onItemClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var allHistoryItems = listOf<HistoryItem>()  // Menyimpan semua data untuk reset filter

    init {
        allHistoryItems = historyItems  // Menyimpan semua data untuk reset filter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = historyItems[position]
        holder.itemName.text = historyItem.name
        holder.itemCategory.text = historyItem.category
        holder.itemAmount.text = formatToRupiah(historyItem.amount)
        holder.itemIcon.setImageResource(historyItem.icon)

        holder.itemView.setOnClickListener {
            onItemClick(historyItem)  // Memanggil listener ketika item diklik
        }
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    // Filter historyItems berdasarkan kategori
    fun filterByCategory(category: String?) {
        historyItems = if (category == null || category == "All Category") {
            allHistoryItems  // Tampilkan semua item jika tidak ada kategori yang dipilih
        } else {
            allHistoryItems.filter { it.category == category }  // Filter berdasarkan kategori
        }
        notifyDataSetChanged()  // Update RecyclerView dengan data yang difilter
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemIcon: ImageView = itemView.findViewById(R.id.itemIcon)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemCategory: TextView = itemView.findViewById(R.id.itemCategory)
        val itemAmount: TextView = itemView.findViewById(R.id.itemAmount)
    }

    private fun formatToRupiah(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    fun getHistoryItems(): List<HistoryItem> {
        return historyItems
    }

}

