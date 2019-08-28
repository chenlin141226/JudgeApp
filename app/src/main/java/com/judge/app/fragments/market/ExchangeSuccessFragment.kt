package com.judge.app.fragments.market

import android.view.View
import androidx.navigation.fragment.findNavController
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.exchangeSuccessFragment

/**
 * @author: jaffa
 * @date: 2019/8/8
 */
class ExchangeSuccessFragment : BaseFragment() {

    override fun epoxyController() = simpleController {
         exchangeSuccessFragment {
             id("exchangeSuccessFragment")
             onclick { _ ->
                 findNavController().navigateUp()
             }
         }
    }

    override fun setToolBar() {
        super.setToolBar()
        toolbar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}