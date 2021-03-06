@file:Suppress("DEPRECATION")

package com.judge.adapters

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.judge.R
import com.judge.data.bean.Data
import com.judge.data.bean.MineItemBean
import com.judge.data.bean.Recommend
import com.judge.data.bean.SettingItemBean
import com.judge.network.ServiceCreator
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
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
        .transition(withCrossFade())
        .error(R.drawable.default_photo)
    if (isPhoto) {
        builder
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .error(R.drawable.default_photo)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(RxImageTool.dp2px(10.0f))))
            .into(imageView)
    } else {
        builder.into(imageView)
    }

}

@BindingAdapter("imageId", requireAll = false)
fun setAllImage(imageView: ImageView, id: Int) {
    imageView.setImageResource(id)
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
            .apply(RequestOptions.bitmapTransform(RoundedCorners(RxImageTool.dp2px(10.0f))))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .error(R.drawable.default_photo).into(imageView)
    } else {
        Glide.with(imageView).load(item.photoUri).placeholder(R.drawable.default_photo)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(RxImageTool.dp2px(10.0f))))
            .error(R.drawable.default_photo).into(imageView)
    }

}

@BindingAdapter("swipeState", requireAll = false)
fun setWipeMenu(swipeMenu: SwipeMenuLayout, isSwipeEnable: Boolean) {
    swipeMenu.isSwipeEnable = isSwipeEnable
    swipeMenu.quickClose()
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

@BindingAdapter("gifImageUrl", requireAll = false)
fun setGifImage(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(imageUrl).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (resource is GifDrawable) {
                resource.setLoopCount(1)
            }
            return false
        }

    }).into(imageView)
}

//根据状态是否发货
@BindingAdapter("bindStatus", requireAll = false)
fun setProductStatus(btn: Button, status: String) {
    when (status) {
        "0" -> {
            btn.text = RxTool.getContext().resources.getString(R.string.nosend)
            btn.background = RxTool.getContext().resources.getDrawable(R.drawable.subscibe)
        }
        "1" -> {
            btn.text = RxTool.getContext().resources.getString(R.string.requet)
            btn.background = RxTool.getContext().resources.getDrawable(R.color.request)
        }
        "2" -> {
            btn.text = RxTool.getContext().resources.getString(R.string.success)
            btn.background = RxTool.getContext().resources.getDrawable(R.color.request)
        }
        "3" -> {
            btn.text = RxTool.getContext().resources.getString(R.string.reject)
            btn.background = RxTool.getContext().resources.getDrawable(R.color.reject)
        }
        "4" -> {
            btn.text = RxTool.getContext().resources.getString(R.string.send)
            btn.background =
                RxTool.getContext().resources.getDrawable(R.drawable.mark_item_exchange)
        }
        "5" -> {
            btn.text = RxTool.getContext().resources.getString(R.string.receive)
            btn.background = RxTool.getContext().resources.getDrawable(R.color.receive)
        }
    }
}

@BindingAdapter("selection",requireAll = false)
fun setSelection(et: EditText,content:String){
    et.setSelection(content.length)
}
