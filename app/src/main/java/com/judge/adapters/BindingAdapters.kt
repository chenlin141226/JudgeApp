package com.judge.adapters

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.judge.R
import com.judge.data.bean.MineItemBean
import com.judge.data.bean.SettingItemBean
import com.judge.network.ServiceCreator
import com.vondear.rxtool.RxTool
import org.jetbrains.anko.textColor

@BindingAdapter("imageUrl", requireAll = false)
fun setImage(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(ServiceCreator.BASE_URL + imageUrl).placeholder(R.drawable.default_message_photo)
        .error(R.drawable.default_message_photo).into(imageView)
}

@BindingAdapter("photoUrl", requireAll = false)
fun setPhotoImage(imageView: ImageView, item: SettingItemBean) {
    if (!TextUtils.isEmpty(item.photoUrl)) {
        Glide.with(imageView).load(item.photoUrl).placeholder(R.drawable.default_photo)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .error(R.drawable.default_photo).into(imageView)
    } else {
        Glide.with(imageView).load(item.photoUri).placeholder(R.drawable.default_photo)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .error(R.drawable.default_photo).into(imageView)
    }

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

@BindingAdapter("colorType", requireAll = false)
fun setColorType(textView: TextView, type: Int) {
    when (type) {
        1 -> textView.textColor = RxTool.getContext().resources.getColor(R.color.colorPrimary)
        3 -> textView.textColor = RxTool.getContext().resources.getColor(R.color.gray)
        else -> textView.textColor = RxTool.getContext().resources.getColor(R.color.black)
    }
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

