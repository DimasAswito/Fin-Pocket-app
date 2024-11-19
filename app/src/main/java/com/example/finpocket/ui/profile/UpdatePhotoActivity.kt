package com.example.finpocket.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.finpocket.databinding.ActivityUpdatePhotoBinding
import java.io.File

class UpdatePhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePhotoBinding
    private var currentImageUri: Uri? = null

    private val galleryLaunch =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.newProfile.setImageURI(uri) // Display the selected image
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

    private val cameraLaunch =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                binding.newProfile.setImageURI(currentImageUri) // Display the taken photo
            } else {
                Toast.makeText(this, "Failed to take photo", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Select image from gallery
        binding.galleryButton.setOnClickListener {
            galleryLaunch.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Take a photo using the camera
        binding.cameraButton.setOnClickListener {
            currentImageUri = getImageUri(this)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            cameraLaunch.launch(currentImageUri!!)
        }

        // Cancel button logic
        binding.cancelButton.setOnClickListener {
            navigateToProfileFragment()
        }

        // Save button logic
        binding.saveButton.setOnClickListener {
            savePhoto()
        }
    }

    private fun navigateToProfileFragment() {
        // Navigate back to ProfileFragment
        finish() // Closes the current activity and goes back to the previous fragment/activity
    }

    private fun savePhoto() {
        // Placeholder logic to save the photo
        Toast.makeText(this, "Foto berhasil disimpan", Toast.LENGTH_SHORT).show()
        navigateToProfileFragment() // Return to ProfileFragment after saving
    }

    private fun getImageUri(context: Context): Uri {
        // Pastikan menggunakan folder yang sesuai dengan konfigurasi file_paths.xml
        val imageFile =
            File.createTempFile("IMG_", ".jpg", context.getExternalFilesDir("Pictures")).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
    }
}
