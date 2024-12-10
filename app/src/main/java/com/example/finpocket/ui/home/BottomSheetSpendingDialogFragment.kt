package com.example.finpocket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.finpocket.databinding.BottomSheetSpendingBinding

class BottomSheetSpendingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSpendingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSpendingBinding.inflate(inflater, container, false)

        // Handle spinner and save action
        val categorySpinner: Spinner = binding.categorySpinner
        binding.saveSpendingButton.setOnClickListener {
            val category = categorySpinner.selectedItem.toString()
            val name = binding.nameEditText.text.toString()
            val amount = binding.amountEditText.text.toString()

            // Process the input data (e.g., save to database or update UI)
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
