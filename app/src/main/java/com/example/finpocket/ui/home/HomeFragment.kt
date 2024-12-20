package com.example.finpocket.ui.home

import android.content.Context
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private val historyItems = mutableListOf<HistoryItem>()  // Daftar untuk menyimpan data history

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        // Load income from SharedPreferences
        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val income = sharedPreferences.getInt("income", 0)
        val formattedIncome = formatToRupiah(income)
        binding.incomeNominal.text = formattedIncome

        // Load spending from SharedPreferences
        val spending = sharedPreferences.getInt("spending", 0)
        val formattedSpending = formatToRupiah(spending)
        binding.spendingNominal.text = formattedSpending

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

    fun formatToRupiah(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    fun updateIncomeDisplay(newIncome: Int) {
        val incomeNominalTextView = binding.root.findViewById<TextView>(R.id.incomeNominal)

        // Format dan tampilkan income yang baru
        val formattedIncome = formatToRupiah(newIncome)
        incomeNominalTextView.text = formattedIncome
    }

    fun updateSpendingDisplay(newSpending: Int) {
        val formattedSpending = formatToRupiah(newSpending)
        binding.spendingNominal.text = formattedSpending
    }

    private fun setupRecyclerView() {

        historyAdapter = HistoryAdapter(historyItems) { historyItem ->
            // Ketika item diklik, panggil fragment HistoryDetailModalFragment
            val detailFragment = HistoryDetailModalFragment.newInstance(historyItem)
            detailFragment.show(parentFragmentManager, "HistoryDetailModal")
        }

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = historyAdapter
    }

    // Menambahkan income
    fun addIncomeToHistory(name: String, amount: Int, date: String) {
        val incomeItem = HistoryItem(
            category = "Income",
            name = name,
            amount = amount,
            icon = R.drawable.income,
            date = date,
            type = "income"
        )

        addHistoryItemToHistory(incomeItem, "income")
    }

    // Menambahkan spending
    fun addSpendingToHistory(spendingItem: HistoryItem) {
        addHistoryItemToHistory(spendingItem, "spending")
    }

    fun addHistoryItemToHistory(item: HistoryItem, type: String) {
        // Tambahkan item ke list dan beri tahu adapter untuk diperbarui
        historyItems.add(0, item)  // Menambahkan di awal

        historyAdapter.notifyDataSetChanged()  // Update RecyclerView

        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val editor = sharedPreferences.edit()

        val jsonHistory = gson.toJson(historyItems)
        editor.putString("history", jsonHistory)

        val currentAmount = sharedPreferences.getInt(type, 0)

        editor.putInt(type, currentAmount + item.amount)

        editor.apply()

        if (type == "income") {
            updateIncomeDisplay(currentAmount + item.amount) // Pastikan tampilan sesuai dengan nilai yang benar
        } else if (type == "spending") {
            updateSpendingDisplay(currentAmount + item.amount)
        }
    }

    private fun setupBudgetRecyclerView() {
        // Ambil total income dari SharedPreferences
        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val totalIncome = sharedPreferences.getInt("income", 1) // Default ke 1 untuk menghindari divisi nol

        // Daftar kategori dan persentase
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
                nominal = (percentage * totalIncome).toInt() // Nominal dihitung dari persentase
            )
        }.take(4) // Ambil hanya 4 data untuk ditampilkan

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

    private fun formatCurrency(amount: String): String {
        // Fungsi untuk memformat nominal menjadi format mata uang
        val number = amount.toIntOrNull()
        return if (number != null) {
            NumberFormat.getInstance(Locale("id", "ID")).format(number)
        } else {
            amount
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
