package com.example.finpocket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finpocket.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
