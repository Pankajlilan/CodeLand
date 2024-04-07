package com.pankaj.codeland.presentation.onboarding

import android.Manifest
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pankaj.codeland.R
import com.pankaj.codeland.databinding.FragmentOnboardingBinding
import com.pankaj.codeland.presentation.preferences.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OnboardingFragment : Fragment() {
  
  private val TAG: String = javaClass.name.toString()
  private lateinit var sharedPreferencesManager: SharedPreferencesManager
  
  private var _binding: FragmentOnboardingBinding? = null
  private val binding: FragmentOnboardingBinding
    get() = _binding!!
  
  private val activityResultLauncher =
    registerForActivityResult(
      ActivityResultContracts.RequestMultiplePermissions()
    )
    { permissions ->
      // Handle Permission granted/rejected
      permissions.entries.forEach {
        Log.d(TAG, "${it.key} = ${it.value}")
      }
    }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
    
    setHyperLinks()
    return _binding?.root
  }
  
  private fun setHyperLinks() {
    val text = getString(R.string.onboarding_content).trimIndent()
    val spannableString = SpannableString(text)
    Linkify.addLinks(spannableString, Linkify.PHONE_NUMBERS)
    binding.appCompatTextView.text = spannableString
    binding.appCompatTextView.movementMethod = LinkMovementMethod.getInstance()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    requestForStoragePermissions()
    sharedPreferencesManager = SharedPreferencesManager(requireContext())
    val retrievedValue = sharedPreferencesManager.getValue()
    retrievedValue?.let {
      navigateToDashboard(it)
    }
    binding.btnLogin.setOnClickListener {
      if (isValidInput()) {
        val username = binding.editTextUsername.text.toString()
        sharedPreferencesManager.saveValue(username)
        navigateToDashboard(username)
      }
    }
  }
  
  private fun isValidInput(): Boolean {
    if (binding.editTextUsername.text.isNullOrEmpty()) {
      binding.editTextUsername.error = getString(R.string.please_enter_username)
      return false
    }
    
    if (binding.editTextPassword.text.isNullOrEmpty()) {
      binding.editTextPassword.error = getString(R.string.please_enter_password)
      return false
    }
    
    return true
  }
  
  private fun navigateToDashboard(username: String?) {
    findNavController().navigate(
      OnboardingFragmentDirections.navigateToDashboardFragment(username = username)
    )
  }
  
  private fun requestForStoragePermissions() {
    CoroutineScope(Dispatchers.IO).launch {
      delay(1000)
      activityResultLauncher.launch(
        arrayOf(
          Manifest.permission.READ_EXTERNAL_STORAGE,
        )
      )
    }
  }
}