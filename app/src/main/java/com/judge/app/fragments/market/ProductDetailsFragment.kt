package com.judge.app.fragments.market

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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
import com.judge.utils.LogUtils
import com.judge.views.productDetailView
import com.vondear.rxtool.view.RxToast
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * @author: jaffa
 * @date: 2019/8/5
 */
data class ProductDetailState(
    val market: CategoryItem,
    val name: String = "",
    val qq: String = "",
    val phone: String = "",
    val adress: String = "",
    val product: ExchangeResult ?= null,
    val productRequest: Async<JsonResponse<ExchangeBean>> = Uninitialized
) : MvRxState {

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

    fun postProduct() = withState { state ->
        val productId = state.market.id_7ree
        val maps = hashMapOf("realname" to state.name,
            "qq" to state.qq,"mobile" to state.phone,"address" to state.adress,"submit_7ree" to "1")
        JudgeRepository.postProduct(productId,maps).subscribeOn(Schedulers.io())
            .doOnError { it.message.let { it1 ->LogUtils.e(it1!!) } }
            .execute {
                copy(productRequest = it,product = it()?.Variables?.data) }
    }

    fun setDefaut(){
     setState { copy(product = null) }
    }

    companion object : MvRxViewModelFactory<ProductDetailViewModel, ProductDetailState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: ProductDetailState
        ): ProductDetailViewModel? {
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
                if(state.name.isEmpty()){
                    context?.let {RxToast.info(it,resources.getString(R.string.product_username),Toast.LENGTH_SHORT,false).show()}
                    return@exchangeClickListener
                }
                if(state.qq.isEmpty()){
                    context?.let {RxToast.info(it,resources.getString(R.string.product_qq),Toast.LENGTH_SHORT,false).show()}
                    return@exchangeClickListener
                }
                if(state.phone.isEmpty()){
                    context?.let {RxToast.info(it,resources.getString(R.string.product_phonee),Toast.LENGTH_SHORT,false).show()}
                    return@exchangeClickListener
                }
                if(state.adress.isEmpty()){
                    context?.let {RxToast.info(it,resources.getString(R.string.product_adress),Toast.LENGTH_SHORT,false).show()}
                    return@exchangeClickListener
                }

                viewModel.postProduct()
            }

            if(state.productRequest is Success&& state.product?.code == "1"){
                context?.let {RxToast.info(it,state.product.success,Toast.LENGTH_SHORT,false).show()}
                runOnUiThread {  navigateTo(R.id.action_marketFragments_to_exchangeSuccessFragment) }
            }else if(state.product?.code == "0"){

                context?.let {RxToast.info(it,state.product.error,Toast.LENGTH_SHORT,false).show()}
            }
        }

    }

    override fun setToolBar() {
        toolbar.visibility = View.VISIBLE
        toolbar.setBackgroundColor(Color.WHITE)
    }

    override fun onDestroyView() {
        viewModel.setDefaut()
        super.onDestroyView()
    }

}