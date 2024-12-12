package com.example.finpocket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.finpocket.R
import com.example.finpocket.model.PlanItem

class PlanAdapter(private val items: List<PlanItem>) :
    RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryIcon: ImageView = view.findViewById(R.id.categoryIcon)
        val categoryName: TextView = view.findViewById(R.id.categoryName)
        val indicator: View = view.findViewById(R.id.indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan_card, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val item = items[position]
        holder.categoryIcon.setImageResource(item.icon)
        holder.categoryName.text = item.name

        // Set indicator height based on percentage
        val cardHeight = holder.itemView.context.resources.getDimension(R.dimen.card_height).toInt()
        val indicatorHeight = (item.percentage * cardHeight).toInt()

        // Update height of indicator
        val layoutParams = holder.indicator.layoutParams
        layoutParams.height = indicatorHeight
        holder.indicator.layoutParams = layoutParams

        // Set indicator color based on percentage range
        holder.indicator.setBackgroundColor(
            when (item.percentage) {
                in 0.0..0.3 -> ContextCompat.getColor(holder.itemView.context, R.color.green)
                in 0.31..0.8 -> ContextCompat.getColor(holder.itemView.context, R.color.yellow)
                else -> ContextCompat.getColor(holder.itemView.context, R.color.red)
            }
        )
    }

    override fun getItemCount(): Int = items.size
}

