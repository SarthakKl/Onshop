package com.example.shoppingapp.Utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.shoppingapp.R

class GlideLoader(val context: Context){
    fun loadPicture(imageUri: Any, image:ImageView){
        Glide
            .with(context)
            .load(imageUri)
            .centerCrop()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(image)
    }
    fun loadProductPicture(imageUri: Any, image:ImageView){
        Glide
            .with(context)
            .load(imageUri)
            .centerCrop()
            .placeholder(R.drawable.image_placeholder)
            .into(image)
    }
}