package com.judge.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl", requireAll = false)
fun setImage(imageView: ImageView, imageUrl: String) {
    Picasso.with(imageView.context).load(imageUrl)
        .into(imageView)
}

