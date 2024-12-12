package com.example.finpocket.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finpocket.R
import com.example.finpocket.adapter.PlanAdapter
import com.example.finpocket.databinding.FragmentPlanBinding
import com.example.finpocket.model.PlanItem

class PlanFragment : Fragment() {

    private var _binding: FragmentPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        val planItems = listOf(
            PlanItem(R.drawable.bills, "Bills", 0.85),
            PlanItem(R.drawable.utilities, "Lifestyle", 0.50),
            PlanItem(R.drawable.transport, "Transport", 0.70),
            PlanItem(R.drawable.groceries, "Grocery", 0.25),
            PlanItem(R.drawable.healthcare, "Health", 0.40),
            PlanItem(R.drawable.entertainment, "Entertainment", 0.90),
            PlanItem(R.drawable.education, "Education", 0.15),
            PlanItem(R.drawable.saving, "Savings", 0.60)
        )

        val adapter = PlanAdapter(planItems)
        binding.budgetRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.budgetRecyclerView.adapter = adapter
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}