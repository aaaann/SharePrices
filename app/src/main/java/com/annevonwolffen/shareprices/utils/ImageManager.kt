package com.annevonwolffen.shareprices.utils

import android.widget.ImageView

interface ImageManager {
    fun load(imageView: ImageView, imageUrl: String)
}