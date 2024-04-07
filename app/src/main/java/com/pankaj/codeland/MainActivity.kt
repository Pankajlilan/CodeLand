package com.pankaj.codeland

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {
  
  private val viewModel : MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen().apply {
      setKeepVisibleCondition{
        viewModel.isLoading.value
      }
    }
    setContentView(R.layout.activity_main)
  }
}