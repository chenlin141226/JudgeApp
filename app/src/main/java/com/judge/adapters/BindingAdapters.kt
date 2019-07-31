package com.judge.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.judge.data.MineItem
import com.squareup.picasso.Picasso
import com.vondear.rxtool.RxTool

@BindingAdapter("imageUrl", requireAll = false)
fun setImage(imageView: ImageView, imageUrl: String) {
    Picasso.with(imageView.context).load(imageUrl)
        .into(imageView)
}

@BindingAdapter("drawableSrc", requireAll = false)
fun setDrawable(textView: TextView, item: MineItem) {
    textView.setCompoundDrawablesWithIntrinsicBounds(
        RxTool.getContext().resources.getDrawable(item.leftIconIdRes),
        null,
        RxTool.getContext().resources.getDrawable(item.rightIconIdRes),
        null
    )
}

