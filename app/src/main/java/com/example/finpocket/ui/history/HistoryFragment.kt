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
import app.futured.donut.DonutSection
import com.example.finpocket.R
import com.example.finpocket.databinding.FragmentHistoryBinding
import com.google.android.material.tabs.TabLayout

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupMonthSpinner()
        setupTabs()
        setupCategorySpinner()

        return root
    }

    private fun setupMonthSpinner() {
        // Tambahkan item "Choose Month" sebagai default di awal daftar
        val months = listOf("Choose Month") + listOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )

        // Buat adapter untuk Spinner
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, months
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter ke Spinner
        binding.monthSpinner.adapter = adapter

        // Listener untuk item yang dipilih
        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedMonth = months[position]
                if (position == 0) {
                    // Jika default item "Choose Month" dipilih
                    Toast.makeText(requireContext(), "Please choose a month", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Jika bulan lain dipilih
                    Toast.makeText(requireContext(), "Selected: $selectedMonth", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun updateDonutChartForIncome() {
        binding.donutChart.submitData(
            listOf(
                DonutSection("Salary", Color.GREEN, 60f),
                DonutSection("Investments", Color.BLUE, 40f)
            )
        )
        binding.donutCenterText.apply {
            text = "Rp.1.200.000"
            setTextColor(Color.GREEN) // Ubah warna teks ke hijau
        }
    }


    private fun updateDonutChartForSpending() {
        binding.donutChart.submitData(
            listOf(
                DonutSection("Rent", Color.RED, 50f),
                DonutSection("Groceries", Color.YELLOW, 30f),
                DonutSection("Entertainment", Color.MAGENTA, 20f)
            )
        )
        binding.donutCenterText.apply {
            text = "Rp.800.000"
            setTextColor(Color.RED) // Ubah warna teks ke merah
        }
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
            "Food",
            "Housing",
            "Transportation",
            "Healthcare",
            "Education",
            "Utilities",
            "Clothing",
            "Entertainment"
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
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val selectedCategory = categories[position]
                    if (position == 0) {
                        // Jika default item "Choose Category" dipilih
                        Toast.makeText(
                            requireContext(), "Please choose a category", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Jika kategori lain dipilih
                        Toast.makeText(
                            requireContext(),
                            "Selected Category: $selectedCategory",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Tidak ada tindakan
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
