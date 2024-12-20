package com.example.finpocket.ui.manualPlanning

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.text.DecimalFormat
import java.text.NumberFormat

class CurrencyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var isUpdating = false
    private val numberFormat: NumberFormat = DecimalFormat("#,###")

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                val currentText = s.toString()
                isUpdating = true
                try {
                    val cleanString = currentText.replace("[,.]".toRegex(), "")
                    if (cleanString.isNotEmpty()) {
                        // Parse and format the number
                        val parsedValue = cleanString.toLongOrNull() ?: 0L
                        val formatted = numberFormat.format(parsedValue)
                        setText(formatted)
                        setSelection(formatted.length)
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    setText("")
                }
                isUpdating = false
            }
        })
    }

    fun getIntValue(): String {
        val cleanString = text.toString().replace("[,.]".toRegex(), "")
        return cleanString
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        val cleanString = text.toString().replace("[,.]".toRegex(), "")
        val formatted = if (cleanString.isNotEmpty()) {
            try {
                numberFormat.format(cleanString.toLongOrNull() ?: 0L)
            } catch (e: NumberFormatException) {
                ""
            }
        } else ""
        super.setText(formatted, type)
    }
}