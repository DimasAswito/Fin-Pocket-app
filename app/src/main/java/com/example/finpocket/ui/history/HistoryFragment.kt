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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadHistoryFromSharedPreferences()  // Load history when the fragment is created
        setupCategorySpinner()
    }

    private fun loadHistoryFromSharedPreferences() {
        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonHistory = sharedPreferences.getString("history", null)

        if (jsonHistory != null) {
            val type = object : TypeToken<List<HistoryItem>>() {}.type
            val allHistoryItems: List<HistoryItem> = gson.fromJson(jsonHistory, type)

            // Update the historyItems list and RecyclerView
            historyItems.clear()
            historyItems.addAll(allHistoryItems)
            historyAdapter.notifyDataSetChanged()
        }
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


    private fun setupMonthSpinner() {
        val months = resources.getStringArray(R.array.months)
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, months
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthSpinner.adapter = adapter

        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
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
        // Ambil nilai income dari SharedPreferences
        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val income = sharedPreferences.getInt("income", 0)

        // Buat data untuk donut chart
        val data = listOf(
            DonutSection(
                "Income",
                Color.parseColor("#7DFFB1"),
                100f
            )  // Anda bisa menyesuaikan persentase jika perlu
        )
        binding.donutChart.submitData(data)

        // Update tampilan dengan nilai income
        binding.donutCenterText.text = "${formatToRupiah(income)}"
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
                tabView, setTabBackground(requireContext())
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
                    0 -> {
                        clearDonutChart() // Pastikan chart dibersihkan sebelum update income
                        updateDonutChartForIncome() // Income
                    }
                    1 -> {
                        clearDonutChart() // Pastikan chart dibersihkan sebelum update spending
                        updateDonutChartForSpending() // Spending
                    }
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


    private fun updateDonutChartForSpending() {
        // Ambil nilai income dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val income = sharedPreferences.getInt("income", 0)

        // Ambil data history dari RecyclerView (historyItems)
        val historyItems = historyAdapter.getHistoryItems() // Pastikan adapter memiliki metode ini
        val categorySpending = mutableMapOf<String, Int>()

        // Hitung total spending per kategori
        for (item in historyItems) {
            if (item.amount > 0 && item.type == "spending") { // Periksa apakah item merupakan pengeluaran
                categorySpending[item.category] = categorySpending.getOrDefault(item.category, 0) + item.amount
            }
        }

        // Tentukan kategori dan persentase yang digunakan untuk chart
        val categories = listOf(
            "Bills" to categorySpending.getOrDefault("Bills", 0),
            "Groceries" to categorySpending.getOrDefault("Groceries", 0),
            "Transport" to categorySpending.getOrDefault("Transport", 0),
            "Entertainment" to categorySpending.getOrDefault("Entertainment", 0),
            "Healthcare" to categorySpending.getOrDefault("Healthcare", 0),
            "Education" to categorySpending.getOrDefault("Education", 0),
            "Utilities" to categorySpending.getOrDefault("Utilities", 0),
            "Savings" to categorySpending.getOrDefault("Savings", 0)
        )

        // Hanya tampilkan kategori yang memiliki spending lebih dari 0
        val data = categories.filter { it.second > 0 }
            .map { (category, amount) ->
                val percentage = (amount.toFloat() / income.toFloat()) * 100f
                val colorResId = resources.getIdentifier("${category.toLowerCase()}_colors", "color", context?.packageName)
                val color = ContextCompat.getColor(requireContext(), colorResId)
                DonutSection(category, color, percentage)
            }

        // Kirim data ke donut chart
        binding.donutChart.submitData(data)

        // Update donut center text dengan total spending
        val totalSpending = categorySpending.values.sum()
        binding.donutCenterText.text = "${formatToRupiah(totalSpending)}"
        binding.donutCenterText.setTextColor(Color.GRAY)
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
            "All Category",
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
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCategory = categories[position]
                    historyAdapter.filterByCategory(
                        if (position == 0) null else selectedCategory
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    fun formatToRupiah(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    fun addIncomeToHistory(name: String, amount: Int, date: String) {
        // Menambahkan item income baru ke daftar history
        val incomeItem = HistoryItem(
            category = "Income",  // Kategori income
            name = name, amount = amount,  // Format menjadi Rupiah
            icon = R.drawable.income,  // Gunakan icon income yang sesuai
            date = date,  // Menambahkan tanggal saat ini
            type = "income"
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
            icon = resources.getIdentifier(
                parts[3].replace("@drawable/", ""), "drawable", requireContext().packageName
            ),
            date = parts[4],
            type = parts[5]
        )
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
