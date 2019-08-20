package com.judge.adapters

import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.judge.R
import com.judge.data.bean.Data
import com.judge.data.bean.MineItemBean
import com.judge.data.bean.Recommend
import com.judge.data.bean.SettingItemBean
import com.judge.network.ServiceCreator
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import com.vondear.rxtool.RxConstants
import com.vondear.rxtool.RxImageTool
import com.vondear.rxtool.RxTool
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter(value = ["imageUrl", "isPhoto"], requireAll = false)
fun setImage(imageView: ImageView, imageUrl: String, isPhoto: Boolean) {
    val url: String = if (imageUrl.contains(ServiceCreator.BASE_URL)) {
        imageUrl
    } else {
        ServiceCreator.BASE_URL + imageUrl
    }
    val builder = Glide.with(imageView).load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .transition(withCrossFade())
        .error(R.drawable.default_message_photo)
    if (isPhoto) {
        builder
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    } else {
        builder.into(imageView)
    }

}

@BindingAdapter("imageAllUrl", requireAll = false)
fun setAllImage(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(imageUrl).placeholder(R.drawable.footbar)
        .error(R.drawable.footbar).into(imageView)
}

@BindingAdapter("photoUrl", requireAll = false)
fun setPhotoImage(imageView: ImageView, item: SettingItemBean) {
    if (!TextUtils.isEmpty(item.photoUrl)) {
        Glide.with(imageView).load(item.photoUrl).placeholder(R.drawable.default_photo)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .error(R.drawable.default_photo).into(imageView)
    } else {
        Glide.with(imageView).load(item.photoUri).placeholder(R.drawable.default_photo)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .error(R.drawable.default_photo).into(imageView)
    }

}

@BindingAdapter("swipeState", requireAll = false)
fun setWipeMenu(swipeMenu: SwipeMenuLayout, ste: String) {
    swipeMenu.smoothClose()
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
    when (whistleType) {
        "金哨子" -> imageView.setImageResource(R.drawable.ic_gold_whistle)
        "银哨子" -> imageView.setImageResource(R.drawable.ic_silver_whistle)
        "铜哨子" -> imageView.setImageResource(R.drawable.ic_copper_whistle)
        else -> imageView.setImageResource(R.drawable.bg_whistle)
    }
}

@BindingAdapter("dataText", requireAll = false)
fun setInforMationText(textView: TextView, item: Data) {
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    val format = sdf.format(Date(item.dateline.toLong() * 1000))
    textView.text = format
}

@BindingAdapter("recommendText", requireAll = false)
fun setrecommendTextText(textView: TextView, item: Recommend) {
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    val format = sdf.format(Date(item.dateline.toLong() * 1000))
    textView.text = format
}

@BindingAdapter("edtionText", requireAll = false)
fun setEditionText(btn: Button, favorite: String) {
    if (favorite == "0") {
        btn.text = "订阅"
        btn.setBackgroundResource(R.drawable.mark_item_exchange)
    } else if (favorite == "1") {
        btn.text = "已订阅"
        btn.setBackgroundResource(R.drawable.subscibe)
    }
}

