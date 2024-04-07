package com.pankaj.codeland.presentation.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pankaj.codeland.R
import com.pankaj.codeland.databinding.FragmentDashboardBinding
import com.pankaj.codeland.presentation.dialog.CustomPhotoDialog
import com.pankaj.codeland.presentation.preferences.SharedPreferencesManager

class DashboardFragment : Fragment() {
  
  private lateinit var sharedPreferencesManager: SharedPreferencesManager
  private var _binding: FragmentDashboardBinding? = null
  private val binding: FragmentDashboardBinding
    get() = _binding!!
  
  private val PICK_IMAGE_REQUEST = 1
  
  private lateinit var imageUri: Uri
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentDashboardBinding.inflate(inflater, container, false)
    
    binding.apply {
      imgBtnLogout.setOnClickListener {
        sharedPreferencesManager.clear()
        findNavController().navigate(DashboardFragmentDirections.navigateToOnboardingFragment())
      }
      btnUpload.setOnClickListener {
        openGallery()
      }
      btnView.setOnClickListener {
        val dialog = CustomPhotoDialog(requireContext(), imageUri)
        dialog.show()
      }
    }
    
    return _binding?.root
  }
  
  @RequiresApi(Build.VERSION_CODES.N)
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    
    sharedPreferencesManager = SharedPreferencesManager(requireContext())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      binding.tvGreetings.text = getGreeting()
    }
    
    requireView().isFocusableInTouchMode = true
    requireView().requestFocus()
    requireView().setOnKeyListener { _, keyCode, _ ->
      if (keyCode == KeyEvent.KEYCODE_BACK) {
        requireActivity().finish()
        return@setOnKeyListener true
      }
      return@setOnKeyListener false
    }
  }
  
  private fun openGallery() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intent, PICK_IMAGE_REQUEST)
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
      val selectedImageUri = data.data
      if (selectedImageUri != null) {
        imageUri = selectedImageUri
      }
      binding.appCompatImageView.setImageURI(imageUri)
      binding.appCompatSampleImageView.setImageURI(null)
    }
  }
  
  @RequiresApi(Build.VERSION_CODES.O)
  fun getGreeting(): String {
    val currentTime = java.time.LocalTime.now()
    return when (currentTime.hour) {
      in 6..11 -> getString(R.string.good_morning)
      in 12..16 -> getString(R.string.good_afternoon)
      in 17..20 -> getString(R.string.good_evening)
      else -> getString(R.string.good_night)
    }
  }
}