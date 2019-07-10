package com.judge.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.judge.R

class MarketFragment : BaseMvRxFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.market_fagment, container, false)
    }

    override fun invalidate() {
        Log.e("Tag", "MarketFragment invalidate")
    }
}