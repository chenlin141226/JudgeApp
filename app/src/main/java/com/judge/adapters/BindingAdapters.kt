package com.judge.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.judge.R
import com.judge.data.MineItemBean
import com.vondear.rxtool.RxTool

@BindingAdapter("imageUrl", requireAll = false)
fun setImage(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(imageUrl).placeholder(R.drawable.default_message_photo)
        .error(R.drawable.default_message_photo).into(imageView)
}


@BindingAdapter("drawableSrc", requireAll = false)
fun setDrawable(textView: TextView, item: MineItemBean) {
    textView.setCompoundDrawablesWithIntrinsicBounds(
        RxTool.getContext().resources.getDrawable(item.leftIconIdRes),
        null,
        RxTool.getContext().resources.getDrawable(item.rightIconIdRes),
        null
    )
}

@BindingAdapter("srcType", requireAll = false)
fun setTypedSrc(imageView: ImageView, type: Int) {
    when (type) {
        0 -> imageView.setImageResource(R.drawable.ic_personal_message)
        1 -> imageView.setImageResource(R.drawable.ic_public_message)
        else -> imageView.setImageResource(R.drawable.ic_personal_message)
    }
}

@BindingAdapter("whistleType", requireAll = false)
fun setWhistleImage(imageView: ImageView, whistleType: String) {
    when (whistleType.toInt()) {
        1 -> imageView.setImageResource(R.drawable.ic_gold_whistle)
        2 -> imageView.setImageResource(R.drawable.ic_silver_whistle)
        3 -> imageView.setImageResource(R.drawable.ic_copper_whistle)
        else -> imageView.setImageResource(R.drawable.ic_copper_whistle)
    }
}

