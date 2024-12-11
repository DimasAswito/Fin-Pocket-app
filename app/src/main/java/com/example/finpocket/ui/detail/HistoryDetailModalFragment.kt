package com.example.finpocket.ui.detail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.finpocket.R
import com.example.finpocket.adapter.HistoryAdapter
import com.example.finpocket.adapter.HistoryAdapter.*
import com.example.finpocket.model.HistoryItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryDetailModalFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_HISTORY = "ARG_HISTORY"

        fun newInstance(historyItem: HistoryItem): HistoryDetailModalFragment {
            return HistoryDetailModalFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_HISTORY, historyItem)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_history_detail_modal, container, false)

        val historyItem = arguments?.getParcelable<HistoryItem>(ARG_HISTORY)
        historyItem?.let {
            view.findViewById<ImageView>(R.id.icon).setImageResource(it.icon)
            view.findViewById<TextView>(R.id.category).text = it.category
            view.findViewById<TextView>(R.id.name_and_date).text =
                Html.fromHtml("${it.name} was recorded on <b>${formatDate(it.date)}</b>", Html.FROM_HTML_MODE_LEGACY)
            view.findViewById<TextView>(R.id.amount).text = "Rp ${it.amount}"
        }

        return view
    }

    private fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate!!)
    }
}

