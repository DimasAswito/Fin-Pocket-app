package com.example.finpocket.ui.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finpocket.R
import com.example.finpocket.adapter.PlanAdapter
import com.example.finpocket.databinding.FragmentPlanBinding
import com.example.finpocket.model.PlanItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.DecimalFormat

class PlanFragment : Fragment() {

    private var _binding: FragmentPlanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        // Ambil total pemasukan dari SharedPreferences
        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val totalIncome = sharedPreferences.getInt("income", 1) // Default to 1 to avoid division by zero

        // Daftar kategori dengan alokasi pengeluaran
        val categories = listOf(
            "Bills" to 0.2f,
            "Groceries" to 0.25f,
            "Transport" to 0.15f,
            "Entertainment" to 0.1f,
            "Healthcare" to 0.1f,
            "Education" to 0.1f,
            "Utilities" to 0.05f,
            "Savings" to 0.05f
        )

        // Buat daftar PlanItem berdasarkan total income
        val planItems = categories.map { (category, percentage) ->
            PlanItem(
                icon = getIconForCategory(category),
                name = category,
                percentage = percentage,
                nominal = (percentage * totalIncome).toInt() // Nominal = persen dari total income
            )
        }

        // Inisialisasi adapter
        val adapter = PlanAdapter(planItems) { planItem ->
            // Tampilkan modal sheet saat item diklik
            val modalView = layoutInflater.inflate(R.layout.modal_budget_detail, null)
            val iconView: ImageView = modalView.findViewById(R.id.icon)
            val categoryView: TextView = modalView.findViewById(R.id.category)
            val descriptionView: TextView = modalView.findViewById(R.id.description)
            val amountView: TextView = modalView.findViewById(R.id.amount)

            // Bind data
            iconView.setImageResource(planItem.icon)
            categoryView.text = planItem.name
            descriptionView.text =
                "This ${planItem.name} was allocated ${"%.0f".format(planItem.percentage * 100)}%"
            amountView.text = "Rp ${planItem.nominal.formatCurrency()}"

            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(modalView)
            bottomSheetDialog.show()
        }

        // Set RecyclerView layout dan adapter
        binding.budgetRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.budgetRecyclerView.adapter = adapter
    }

    private fun getIconForCategory(category: String): Int {
        return when (category) {
            "Bills" -> R.drawable.bills
            "Groceries" -> R.drawable.groceries
            "Transport" -> R.drawable.transport
            "Entertainment" -> R.drawable.entertainment
            "Healthcare" -> R.drawable.healthcare
            "Education" -> R.drawable.education
            "Utilities" -> R.drawable.utilities
            "Savings" -> R.drawable.savings
            else -> R.drawable.ic_baseline_wallet_24
        }
    }

    fun Int.formatCurrency(): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
