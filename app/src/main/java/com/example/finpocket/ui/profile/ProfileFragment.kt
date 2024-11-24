package com.example.finpocket.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.finpocket.databinding.FragmentProfileBinding
import com.example.finpocket.ui.register.RegisterActivity
import com.example.finpocket.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up click listeners for TextViews
        setupClickListeners()

        return root
    }

    private fun setupClickListeners() {
        // Navigate to Update Photo Activity
        binding.updatePhoto.setOnClickListener {
            val intent = Intent(requireContext(), UpdatePhotoActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Change Password Activity
        binding.forgotPassword.setOnClickListener{
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        // Navigate to About Us Activity
        binding.about.setOnClickListener {
            val intent = Intent(requireContext(), AboutUsActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Help Activity
        binding.help.setOnClickListener {
            val intent = Intent(requireContext(), HelpActivity::class.java)
            startActivity(intent)
        }

        // Show logout confirmation dialog
        binding.logout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Yes") { dialog, _ ->
                // Perform logout action
                logout()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun logout() {
        // Perform logout logic here (e.g., clear session, redirect to login)
        Toast.makeText(requireContext(), "You have been logged out", Toast.LENGTH_SHORT).show()
        // Redirect to WelcomeActivity
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
