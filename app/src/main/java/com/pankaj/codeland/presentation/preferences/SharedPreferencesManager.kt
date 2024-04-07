package com.pankaj.codeland.presentation.preferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
  private val PREF_NAME = "MyAppPreferences"
  private val KEY_VALUE = "username"
  
  private val sharedPreferences: SharedPreferences =
    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
  private val editor: SharedPreferences.Editor = sharedPreferences.edit()
  
  fun saveValue(value: String) {
    editor.putString(KEY_VALUE, value)
    editor.apply()
  }
  
  fun getValue(): String? {
    return sharedPreferences.getString(KEY_VALUE, null)
  }
  
  fun clear() {
    editor.clear().apply()
  }
}
