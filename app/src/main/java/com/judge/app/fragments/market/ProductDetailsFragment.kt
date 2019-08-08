package com.judge.app.fragments.market

import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.MarketBean
import com.judge.views.productDetailView
import com.vondear.rxtool.view.RxToast

/**
 * @author: jaffa
 * @date: 2019/8/5
 */
data class ProductDetailState(
    val market: MarketBean,
    val name: String? = null,
    val qq: String? = null,
    val phone: String? = null,
    val adress: String? = null
) : MvRxState {

    constructor(args: MarketBean) : this(market = args)
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

    companion object : MvRxViewModelFactory<ProductDetailViewModel, ProductDetailState> {
        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: ProductDetailState): ProductDetailViewModel? {
            return ProductDetailViewModel(state)
        }
    }
}

class ProductDetailsFragment : BaseFragment() {
    private val viewModel: ProductDetailViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->
        productDetailView {
            id("ProductDetailsFragment")
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
                viewModel.selectSubscribe(ProductDetailState::qq, ProductDetailState::adress) { qq, adress ->
                    context?.let {
                        RxToast.info(it, qq.toString()+adress.toString(), Toast.LENGTH_SHORT, true).show()
                    }
                }
            }
        }

    }

    override fun setToolBar() {
        toolbar.visibility = View.VISIBLE
        toolbar.setBackgroundColor(Color.WHITE)
       // sharedViewModel.setVisible(false)
    }
}