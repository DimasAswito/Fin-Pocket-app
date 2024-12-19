package com.example.finpocket.ui.history

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.futured.donut.DonutSection
import com.example.finpocket.R
import com.example.finpocket.adapter.HistoryAdapter
import com.example.finpocket.databinding.FragmentHistoryBinding
import com.example.finpocket.model.HistoryItem
import com.example.finpocket.ui.detail.HistoryDetailModalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import java.text.NumberFormat
import java.util.Locale

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private val historyItems = mutableListOf<HistoryItem>()  // Daftar untuk menyimpan data history

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setBottomMarginForView(binding.scrollHistoryRecyclerView)

        setupMonthSpinner()
        setupTabs()
        setupCategorySpinner()

        return root
    }

    private fun setupMonthSpinner() {
        val months = resources.getStringArray(R.array.months)
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, months
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthSpinner.adapter = adapter

        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMonth = months[position]
                if (selectedMonth == "December") {
                    updateDonutChartForIncome()
                } else {
                    clearDonutChart()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateDonutChartForIncome() {
        val data = listOf(
            DonutSection("Income", Color.parseColor("#7DFFB1"), 100f)
        )
        binding.donutChart.submitData(data)
        binding.donutCenterText.text = "Rp.5.200.000"
        binding.donutCenterText.setTextColor(Color.GRAY)
    }

    private fun updateDonutChartForSpending() {
        val data = listOf(
            DonutSection("Bills", Color.parseColor("#69AAFF"), 20f),
            DonutSection("Groceries", Color.parseColor("#FFA274"), 25f),
            DonutSection("Transport", Color.parseColor("#C399FF"), 15f),
            DonutSection("Entertainment", Color.parseColor("#65B5FF"), 10f),
            DonutSection("Healthcare", Color.parseColor("#FFD958"), 10f),
            DonutSection("Education", Color.parseColor("#FFD958"), 10f),
            DonutSection("Utilities", Color.parseColor("#FF7F7F"), 5f),
            DonutSection("Savings", Color.parseColor("#4CAF50"), 5f)
        )
        binding.donutChart.submitData(data)
        binding.donutCenterText.text = "Rp.3.400.000"
        binding.donutCenterText.setTextColor(Color.GRAY)
    }

    private fun clearDonutChart() {
        binding.donutChart.submitData(emptyList())
        binding.donutCenterText.text = "No Data"
        binding.donutCenterText.setTextColor(Color.GRAY)
    }



    private fun setupTabs() {
        // Tambahkan tab ke TabLayout
        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Income"))
        tabLayout.addTab(tabLayout.newTab().setText("Spending"))

        // Sesuaikan tampilan setiap tab
        val tabCount: Int = tabLayout.tabCount
        for (i in 0 until tabCount) {
            val tabView: View = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            tabView.requestLayout() // Pastikan layout diperbarui
            ViewCompat.setBackground(
                tabView,
                setTabBackground(requireContext())
            ) // Tambahkan background state
            ViewCompat.setPaddingRelative(
                tabView,
                tabView.paddingStart,
                tabView.paddingTop,
                tabView.paddingEnd,
                tabView.paddingBottom
            )
        }

        // Set listener untuk pergantian tab
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Tab yang dipilih
                val selectedPosition = tab?.position
                when (selectedPosition) {
                    0 -> updateDonutChartForIncome() // Income
                    1 -> updateDonutChartForSpending() // Spending
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Tidak diperlukan
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Tidak diperlukan
            }
        })

        // Pilih tab default (Income)
        tabLayout.getTabAt(0)?.select()
        updateDonutChartForIncome()
    }

    // Fungsi untuk menetapkan background dengan StateListDrawable
    private fun setTabBackground(context: Context): StateListDrawable {
        val states = StateListDrawable()
        // Background ketika tab dipilih
        states.addState(
            intArrayOf(android.R.attr.state_selected),
            ContextCompat.getDrawable(context, R.drawable.tab_bg_normal_green)
        )
        // Background ketika tab tidak dipilih
        states.addState(
            intArrayOf(-android.R.attr.state_selected),
            ContextCompat.getDrawable(context, R.drawable.tab_bg_normal)
        )
        return states
    }

    private fun setupCategorySpinner() {
        // Daftar kategori kebutuhan hidup
        val categories = listOf(
            "Choose Category",
            "Bills",
            "Groceries",
            "Transport",
            "Entertainment",
            "Healthcare",
            "Education",
            "Utilities",
            "Savings",
            "Income"
        )

        // Buat adapter untuk Spinner
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter ke Spinner
        binding.categorySpinner.adapter = adapter

        // Listener untuk item yang dipilih
//        binding.categorySpinner.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    val selectedCategory = resources.getStringArray(R.array.categories)[position]
//                    historyAdapter.filterByCategory(if (position == 0) null else selectedCategory)
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {}
//            }

    }
    private fun formatCurrency(amount: Int): String {
        val localeID = Locale("in", "ID") // Gunakan locale Indonesia
        val formatter = NumberFormat.getNumberInstance(localeID)
        return formatter.format(amount)
    }
    fun formatToRupiah(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount).replace("Rp", "Rp ").replace(",00", "")
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

    fun addIncomeToHistory(name: String, amount: Int, date: String) {
        // Menambahkan item income baru ke daftar history
        val incomeItem = HistoryItem(
            category = "Income",  // Kategori income
            name = name,
            amount = amount,  // Format menjadi Rupiah
            icon = R.drawable.income,  // Gunakan icon income yang sesuai
            date = date  // Menambahkan tanggal saat ini
        )

        // Tambahkan item ke list dan beri tahu adapter untuk diperbarui
        historyItems.add(0, incomeItem)  // Menambahkan di awal
        historyAdapter.notifyItemInserted(0)  // Update RecyclerView
    }


    private fun showHistoryDetail(historyItem: HistoryItem) {
        val modalSheet = HistoryDetailModalFragment.newInstance(historyItem)
        modalSheet.show(parentFragmentManager, "HistoryDetailModal")
    }

    private fun parseHistory(data: String): HistoryItem {
        val parts = data.split("|")
        return HistoryItem(
            category = parts[0],
            name = parts[1],
            amount = (parts[2].toInt()),
            icon = resources.getIdentifier(parts[3].replace("@drawable/", ""), "drawable", requireContext().packageName),
            date = parts[4]
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCategorySpinner()
    }

    private fun setBottomMarginForView(view: View) {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.post {
            val bottomNavHeight = bottomNav.height
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = bottomNavHeight
            view.layoutParams = layoutParams
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
