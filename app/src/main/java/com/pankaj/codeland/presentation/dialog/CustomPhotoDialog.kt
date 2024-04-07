package com.pankaj.codeland.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import com.github.chrisbanes.photoview.PhotoView
import com.pankaj.codeland.R

class CustomPhotoDialog(context: Context, imageUrl: Uri) : Dialog(context) {
  
  init {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(R.layout.dialog_image_viewer)
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    
    val photoView = findViewById<PhotoView>(R.id.photo_view)
    val closeButton = findViewById<ImageButton>(R.id.close_button)
    photoView.setImageURI(imageUrl)
    closeButton.setOnClickListener {
      dismiss()
    }
  }
}


