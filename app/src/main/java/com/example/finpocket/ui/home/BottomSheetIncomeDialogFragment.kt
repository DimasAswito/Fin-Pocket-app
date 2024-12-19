package com.example.finpocket.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.finpocket.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BottomSheetIncomeDialogFragment : BottomSheetDialogFragment() {

    private lateinit var nameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var saveIncomeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_income, container, false)

        nameEditText = view.findViewById(R.id.incomeNameEditText)
        amountEditText = view.findViewById(R.id.amountIncomeEditText)
        saveIncomeButton = view.findViewById(R.id.saveIncomeButton)

        saveIncomeButton.setOnClickListener {
            val incomeName = nameEditText.text.toString()
            val amount = amountEditText.text.toString()

            if (incomeName.isNotEmpty() && amount.isNotEmpty()) {
                // Mengambil nilai income yang sudah ada
                val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                val existingIncome = sharedPreferences.getInt("income", 0)

                // Menambahkan income baru
                val newIncome = existingIncome + amount.toInt()

                // Menyimpan nilai income baru ke SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putInt("income", newIncome)
                editor.apply()

                // Tampilkan toast untuk konfirmasi
                Toast.makeText(context, "Income Saved: $incomeName - Rp ${formatToRupiah(newIncome)}", Toast.LENGTH_SHORT).show()

                // Tutup bottom sheet
                dismiss()

                // Memperbarui tampilan income di HomeFragment
                val homeFragment = parentFragment as? HomeFragment
                homeFragment?.updateIncomeDisplay(newIncome)

                // Menambahkan income baru ke RecyclerView dengan tanggal hari ini
                val currentDate = getCurrentDate()
                homeFragment?.addIncomeToHistory(incomeName, amount.toInt(), currentDate)

            } else {
                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun formatToRupiah(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    // Fungsi untuk mendapatkan tanggal saat ini dalam format "dd-MM-yyyy"
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        return dateFormat.format(Date())
    }
}


