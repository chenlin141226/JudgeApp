package com.judge.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.judge.R

class MineFragment : BaseMvRxFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mine_fragment, container, false)
    }

    override fun invalidate() {
        Log.e("Tag", "MineFragment invalidate")
    }

}