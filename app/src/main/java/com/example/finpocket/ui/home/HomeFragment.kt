package com.example.finpocket.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finpocket.R
import com.example.finpocket.adapter.HistoryAdapter
import com.example.finpocket.adapter.PlanAdapter
import com.example.finpocket.databinding.FragmentHomeBinding
import com.example.finpocket.model.HistoryItem
import com.example.finpocket.model.PlanItem
import com.example.finpocket.ui.detail.HistoryDetailModalFragment
import com.example.finpocket.ui.history.HistoryFragment
import com.example.finpocket.ui.plan.PlanFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        setBottomMarginForView(binding.scrollViewHome)

        // Set up RecyclerView
        setupRecyclerView()

        binding.showMore.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_plan)
        }

        setupBudgetRecyclerView()

        // Set up click listener for "See More" TextView
        binding.seeMoreHistory.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_history)
        }

        binding.addIncome.setOnClickListener {
            // Show BottomSheet when "Add Income" is clicked
            val bottomSheetFragment = BottomSheetIncomeDialogFragment()
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        binding.addSpending.setOnClickListener {
            // Show BottomSheet when "Add Spending" is clicked
            val bottomSheetFragment = BottomSheetSpendingDialogFragment()
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        return root
    }

    private fun setupRecyclerView() {
        val historyData = resources.getStringArray(R.array.history_data).take(5).map { item ->
            val parts = item.split("|")
            HistoryItem(
                category = parts[0],
                name = parts[1],
                amount = formatCurrency(parts[2]),
                icon = resources.getIdentifier(parts[3].replace("@drawable/", ""), "drawable", requireContext().packageName),
                date = parts[4]
            )
        }

        // Initialize adapter
        historyAdapter = HistoryAdapter(historyData)
        historyAdapter.setOnItemClickListener { historyItem ->
            val bottomSheetFragment = HistoryDetailModalFragment.newInstance(historyItem)
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun setupBudgetRecyclerView() {
        val planItems = listOf(
            PlanItem(R.drawable.bills, "Bills", 0.85, 350000),
            PlanItem(R.drawable.utilities, "Lifestyle", 0.50, 330000),
            PlanItem(R.drawable.transport, "Transport", 0.70, 400000),
            PlanItem(R.drawable.groceries, "Grocery", 0.25, 800000),
//            PlanItem(R.drawable.bills, "Bills", 0.2, 350000),
//            PlanItem(R.drawable.utilities, "Lifestyle", 0.0, 330000),
//            PlanItem(R.drawable.transport, "Transport", 0.0, 400000),
//            PlanItem(R.drawable.groceries, "Grocery", 0.0, 800000),
            PlanItem(R.drawable.healthcare, "Healthcare", 0.40, 500000),
            PlanItem(R.drawable.entertainment, "Entertainment", 0.90, 300000),
            PlanItem(R.drawable.education, "Education", 0.15, 500000),
            PlanItem(R.drawable.saving, "Savings", 0.60,70000)
        ).take(4) // Ambil hanya 4 data

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
            amountView.text = "Rp ${planItem.nominal.formatCurrency()}" // Update nilai aktual sesuai kebutuhan

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

    private fun formatCurrency(amount: String): String {
        // Fungsi untuk memformat nominal menjadi format mata uang
        val number = amount.toIntOrNull()
        return if (number != null) {
            NumberFormat.getInstance(Locale("id", "ID")).format(number)
        } else {
            amount
        }
    }

//    private fun setBottomMarginForView(view: View) {
//        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
//        bottomNav.post {
//            val bottomNavHeight = bottomNav.height
//            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
//            layoutParams.bottomMargin = bottomNavHeight
//            view.layoutParams = layoutParams
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
