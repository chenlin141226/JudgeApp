package com.judge.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.judge.R
import com.judge.data.MarketBean
import com.judge.extensions.setTextIfDifferent
import kotlinx.android.synthetic.main.product_detail_item.view.*

/**
 * @author: jaffa
 * @date: 2019/8/5
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class ProductDetailView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val userNameWatcher = SimpleTextWatcher{onUserNameChanged?.invoke(it)}
    private val QQWatcher = SimpleTextWatcher{onQQChanged?.invoke(it)}
    private val phoneNumberWatcher = SimpleTextWatcher { onPhoneChanged?.invoke(it) }
    private val adressWatcher = SimpleTextWatcher { onAdressChanged?.invoke(it) }

    init {
       View.inflate(context, R.layout.product_detail_item,this)

        et_name.addTextChangedListener(userNameWatcher)
        et_qq.addTextChangedListener(QQWatcher)
        et_phone.addTextChangedListener(phoneNumberWatcher)
        et_adress.addTextChangedListener(adressWatcher)

    }

    @ModelProp
    fun setGoodsDetail(marker: MarketBean){
        Glide.with(context).load(marker.marketUrl).into(iv_topUrl)
        tv_goodsCategory.text = marker.marketName
        tv_goodsPrice.text = marker.marketNumber
    }


    @TextProp
    fun setUserName(username : CharSequence?){
        et_name.setTextIfDifferent(username)
    }

    @TextProp
    fun setQQ(username : CharSequence?){
        et_qq.setTextIfDifferent(username)
    }

    @TextProp
    fun setPhone(username : CharSequence?){
        et_phone.setTextIfDifferent(username)
    }

    @TextProp
    fun setAdress(username : CharSequence?){
        et_adress.setTextIfDifferent(username)
    }

    @set:CallbackProp
    var onUserNameChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onQQChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onPhoneChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onAdressChanged : ((newText : String) -> Unit)? =null

    //兑换点击事件
    @CallbackProp
    fun setExchangeClickListener(listener: OnClickListener?){
        btn_exchange.setOnClickListener(listener)
    }
}

