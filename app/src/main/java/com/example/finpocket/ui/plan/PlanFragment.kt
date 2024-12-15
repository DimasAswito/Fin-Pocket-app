package com.example.finpocket.ui.plan

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

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        val planItems = listOf(
            PlanItem(R.drawable.bills, "Bills", 0.85, 350000),
            PlanItem(R.drawable.utilities, "Lifestyle", 0.50, 330000),
            PlanItem(R.drawable.transport, "Transport", 0.70, 400000),
            PlanItem(R.drawable.groceries, "Grocery", 0.25, 800000),
            PlanItem(R.drawable.healthcare, "Healthcare", 0.40, 500000),
            PlanItem(R.drawable.entertainment, "Entertainment", 0.90, 300000),
            PlanItem(R.drawable.education, "Education", 0.15, 500000),
            PlanItem(R.drawable.saving, "Savings", 0.60,70000)
        )

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
            descriptionView.text = "This ${planItem.name} was filled ${"%.0f".format(planItem.percentage * 100)}%"
            amountView.text = "Rp ${planItem.nominal.formatCurrency()}"// Update nilai aktual sesuai kebutuhan

            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(modalView)
            bottomSheetDialog.show()
        }

        binding.budgetRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.budgetRecyclerView.adapter = adapter
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