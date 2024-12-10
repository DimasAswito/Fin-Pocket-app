package com.example.finpocket.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.example.finpocket.databinding.FragmentProfileBinding
import com.example.finpocket.ui.register.RegisterActivity
import com.example.finpocket.ui.welcome.WelcomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi FirebaseAuth
        auth = Firebase.auth

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
                performLogout()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun performLogout() {
        lifecycleScope.launch {
            try {
                // Hapus kredensial dan sign out dari Firebase
                val credentialManager = CredentialManager.create(requireContext())
                auth.signOut()
                credentialManager.clearCredentialState(ClearCredentialStateRequest())

                // Tampilkan pesan berhasil
                Toast.makeText(requireContext(), "You have been logged out", Toast.LENGTH_SHORT).show()

                // Redirect ke WelcomeActivity
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } catch (e: Exception) {
                // Log jika ada error
                Log.e("ProfileFragment", "Logout error: ${e.message}")
                Toast.makeText(requireContext(), "Logout failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
