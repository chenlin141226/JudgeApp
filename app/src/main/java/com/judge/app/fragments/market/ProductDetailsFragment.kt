package com.judge.app.fragments.market

import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.*
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.CategoryItem
import com.judge.data.bean.ExchangeBean
import com.judge.data.bean.ExchangeResult
import com.judge.data.repository.JudgeRepository
import com.judge.network.JsonResponse
import com.judge.views.productDetailView
import com.vondear.rxtool.RxConstTool
import com.vondear.rxtool.view.RxToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern

/**
 * @author: jaffa
 * @date: 2019/8/5
 */
data class ProductDetailState(val market: CategoryItem,
                              val name: String = "",
                              val qq: String = "",
                              val phone: String = "",
                              val adress: String = "") : MvRxState {

    constructor(args: CategoryItem) : this(market = args)
}

class ProductDetailViewModel(state: ProductDetailState) : MvRxViewModel<ProductDetailState>(state) {

    fun setName(name: String) {
        setState { copy(name = name) }
    }

    fun setQQ(qq: String) {
        setState { copy(qq = qq) }
    }

    fun setPhone(phone: String) {
        setState { copy(phone = phone) }
    }

    fun setAdress(adress: String) {
        setState { copy(adress = adress) }
    }

    //    fun postProduct() = withState { state ->
    //        val productId = state.market.id_7ree
    //        val maps = hashMapOf("realname" to state.name,
    //            "qq" to state.qq,"mobile" to state.phone,"address" to state.adress,"submit_7ree" to "1")
    //        JudgeRepository.postProduct(productId,maps).subscribeOn(Schedulers.io())
    //            .doOnError { it.message.let { it1 ->LogUtils.e(it1!!) } }
    //            .execute {
    //                copy(productRequest = it,product = it()?.Variables?.data) }
    //    }


    companion object : MvRxViewModelFactory<ProductDetailViewModel, ProductDetailState> {
        @JvmStatic
        override fun create(viewModelContext: ViewModelContext,
                            state: ProductDetailState): ProductDetailViewModel? {
            return ProductDetailViewModel(state)
        }
    }
}

class ProductDetailsFragment : BaseFragment() {
    private val viewModel: ProductDetailViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->
        productDetailView {
            id(state.market.id_7ree)
            goodsDetail(state.market)
            userName(state.name)
            qQ(state.qq)
            phone(state.phone)
            adress(state.adress)
            onUserNameChanged { viewModel.setName(it) }
            onQQChanged { viewModel.setQQ(it) }
            onPhoneChanged { viewModel.setPhone(it) }
            onAdressChanged { viewModel.setAdress(it) }

            exchangeClickListener { _ ->
                if (markword(state)) return@exchangeClickListener

                val productId = state.market.id_7ree

                val maps = hashMapOf("realname" to state.name,
                    "qq" to state.qq,
                    "mobile" to state.phone,
                    "address" to state.adress,
                    "submit_7ree" to "1")

                JudgeRepository.postProduct(productId, maps).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { its ->

                        if (its.Variables.data.code == "1") {
                            context?.let {
                                RxToast.info(it, "兑换成功", Toast.LENGTH_SHORT, false).show()
                            }
                            navigateTo(R.id.action_marketFragments_to_exchangeSuccessFragment)
                        } else if (its.Variables.data.code == "0") {
                            context?.let {
                                RxToast.info(it,
                                    its.Variables.data.error,
                                    Toast.LENGTH_SHORT,
                                    false).show()
                            }
                        }
                    }
            }

        }

    }

    private fun markword(state: ProductDetailState): Boolean {
        if (state.name.isEmpty()) {
            context?.let {
                RxToast.info(it,resources.getString(R.string.product_username),Toast.LENGTH_SHORT,false).show()
            }
            return true
        }
        if (state.qq.isEmpty()) {
            context?.let {RxToast.info(it,resources.getString(R.string.product_qq),Toast.LENGTH_SHORT,false).show()
            }
            return true
        }
        if (state.phone.isEmpty()) {
            context?.let {RxToast.info(it, resources.getString(R.string.product_phonee),Toast.LENGTH_SHORT,false).show() }
            return true
        }
        if (!Pattern.compile(RxConstTool.REGEX_MOBILE_EXACT).matcher(state.phone).matches()) {
            context?.let {RxToast.info(it, "请输入正确的手机号码",Toast.LENGTH_SHORT,false).show() }
            return true
        }
        if (state.adress.isEmpty()) {
            context?.let {RxToast.info(it,resources.getString(R.string.product_adress),Toast.LENGTH_SHORT,false).show()}
            return true
        }
        return false
    }

    override fun setToolBar() {
        toolbar.visibility = View.VISIBLE
        toolbar.setBackgroundColor(Color.WHITE)
        sharedViewModel.setVisible(false)
    }

}