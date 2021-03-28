package com.annevonwolffen.shareprices.utils

import android.content.Context
import android.widget.ImageView
import com.annevonwolffen.shareprices.R
import com.bumptech.glide.Glide

class GlideImageManager(private val context: Context) : ImageManager {
    override fun load(imageView: ImageView, imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_monetization)
            .error(R.drawable.ic_monetization)
            .into(imageView)
    }
}