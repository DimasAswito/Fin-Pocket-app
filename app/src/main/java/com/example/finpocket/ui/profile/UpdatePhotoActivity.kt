package com.example.finpocket.ui.profile

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.finpocket.api.ApiConfig
import com.example.finpocket.databinding.ActivityUpdatePhotoBinding
import com.example.finpocket.pref.PreferenceHelper
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UpdatePhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePhotoBinding
    private var currentImageUri: Uri? = null
    private var currentImageFile: File? = null

    private val galleryLaunch =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                currentImageUri = uri
                binding.newProfile.setImageURI(uri) // Tampilkan gambar
                currentImageFile = createFileFromUri(uri) // Konversi ke file
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

    private val cameraLaunch =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && currentImageFile != null) {
                binding.newProfile.setImageURI(currentImageUri) // Tampilkan foto
            } else {
                Toast.makeText(this, "Failed to take photo", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pilih gambar dari galeri
        binding.galleryButton.setOnClickListener {
            galleryLaunch.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Ambil foto dengan kamera
        binding.cameraButton.setOnClickListener {
            currentImageFile = getImageFile(this)
            currentImageUri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.provider",
                currentImageFile!!
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            cameraLaunch.launch(currentImageUri!!)
        }

        // Tombol batal
        binding.cancelButton.setOnClickListener {
            navigateToProfileFragment()
        }

        // Tombol simpan
        binding.saveButton.setOnClickListener {
            savePhoto()
        }
    }

    private fun savePhoto() {
        val userId = PreferenceHelper.getUserId(this)
        if (userId == null) {
            Toast.makeText(this, "User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        val imageFile = currentImageFile ?: run {
            Toast.makeText(this, "No photo to upload", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("picture", imageFile.name, requestFile)
        Log.d(TAG, "Uploading file: ${imageFile.name}, Path: ${imageFile.absolutePath}")

        lifecycleScope.launch {
            try {
                val response = ApiConfig.instance.uploadProfilePicture(userId, body)
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdatePhotoActivity, "Foto berhasil diunggah", Toast.LENGTH_SHORT).show()
                    navigateToProfileFragment()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Failed to upload photo: $errorBody")
                    Toast.makeText(this@UpdatePhotoActivity, "Gagal mengunggah foto: $errorBody", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error uploading photo: ${e.message}")
                Toast.makeText(this@UpdatePhotoActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToProfileFragment() {
        finish() // Tutup activity
    }

    private fun getImageFile(context: Context): File {
        val storageDir = context.getExternalFilesDir("Pictures")
        return File.createTempFile("IMG_", ".jpg", storageDir)
    }

    private fun createFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("IMG_", ".jpg", cacheDir)
        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return tempFile
    }
}
