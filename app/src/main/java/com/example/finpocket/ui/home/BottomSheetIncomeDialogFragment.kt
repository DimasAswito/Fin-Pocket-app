package com.example.finpocket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.finpocket.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetIncomeDialogFragment : BottomSheetDialogFragment() {

    private lateinit var nameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var saveIncomeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_income, container, false)

        nameEditText = view.findViewById(R.id.nameEditText)
        amountEditText = view.findViewById(R.id.amountEditText)
        saveIncomeButton = view.findViewById(R.id.saveIncomeButton)

        saveIncomeButton.setOnClickListener {
            val incomeName = nameEditText.text.toString()
            val amount = amountEditText.text.toString()

            if (incomeName.isNotEmpty() && amount.isNotEmpty()) {
                // Handle the input data here, e.g., save it to ViewModel or pass to activity
                // You can show a Toast message, update UI, or store data.
                Toast.makeText(context, "Income Saved: $incomeName - $amount", Toast.LENGTH_SHORT).show()
                dismiss()  // Close the bottom sheet
            } else {
                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
