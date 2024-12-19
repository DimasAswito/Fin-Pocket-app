package com.example.finpocket.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.finpocket.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.finpocket.databinding.BottomSheetSpendingBinding
import com.example.finpocket.model.HistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BottomSheetSpendingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSpendingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSpendingBinding.inflate(inflater, container, false)

        // Handle spinner and save action
        val categorySpinner: Spinner = binding.categorySpending
        val categories = resources.getStringArray(R.array.spending_categories)

        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        binding.saveSpendingButton.setOnClickListener {
            val category = categorySpinner.selectedItem.toString()
            val name = binding.SpendingNameEditText.text.toString()
            val amountStr = binding.spendingAmountEditText.text.toString()

            if (amountStr.isNotEmpty()) {
                val amount = amountStr.toInt()

                // Save spending to SharedPreferences
                val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val currentSpending = sharedPreferences.getInt("spending", 0)
                editor.putInt("spending", currentSpending + amount)  // Update total spending
                editor.apply()

                // Add spending to history
                val spendingItem = HistoryItem(
                    category = category,
                    name = name,
                    amount = amount,  // Save as string in the HistoryItem
                    icon = resources.getIdentifier(category.toLowerCase(), "drawable", requireContext().packageName),  // Get icon from drawable
                    date = getCurrentDate()  // Save current date
                )

                // Add to history and update RecyclerView
                val homeFragment = parentFragment as HomeFragment
                homeFragment.addSpendingToHistory(spendingItem)

                dismiss()
            } else {
                Toast.makeText(requireContext(), "Amount cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())  // Get current date in yyyy-MM-dd format
    }
}
