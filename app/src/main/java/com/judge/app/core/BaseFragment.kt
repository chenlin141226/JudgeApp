package com.judge.app.core

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import com.judge.R
import com.judge.utils.NetworkUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import org.jetbrains.anko.AnkoLogger

abstract class BaseFragment : BaseMvRxFragment(), AnkoLogger {

    protected lateinit var recyclerView: EpoxyRecyclerView
    protected lateinit var toolbar: Toolbar
    protected lateinit var refreshLayout: SmartRefreshLayout
    //protected lateinit var coordinatorLayout: CoordinatorLayout
    protected val epoxyController by lazy { epoxyController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        NetworkUtils.setOnChangeInternetListener(object : NetworkUtils.OnChangeInternetListener {
            override fun changeInternet(flag: Boolean) {
                onNetWorkChanged(flag)
            }
        })
        return inflater.inflate(R.layout.fragment_base_mvrx, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            toolbar = findViewById(R.id.toolbar)
            refreshLayout = findViewById(R.id.refreshLayout)
            // coordinatorLayout = findViewById(R.id.coordinator_layout)
            recyclerView.setController(epoxyController)
            initRefreshLayout()
            initData()
            toolbar.setupWithNavController(findNavController())
        }
    }

    override fun invalidate() {
        recyclerView.requestModelBuild()
    }

    /**
     * Provide the EpoxyController to use when building models for this Fragment.
     * Basic usages can simply use [simpleController]
     */
    abstract fun epoxyController(): MvRxEpoxyController

    fun onNetWorkChanged(state: Boolean) {
        if (state) initData()
    }

    open fun initRefreshLayout() {}

    open fun initData() {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        epoxyController.cancelPendingModelBuild()
        super.onDestroyView()
    }

    protected fun navigateTo(@IdRes actionId: Int, arg: Parcelable? = null) {
        /**
         * If we put a parcelable arg in [MvRx.KEY_ARG] then MvRx will attempt to call a secondary
         * constructor on any MvRxState objects and pass in this arg directly.
         * @see [com.airbnb.mvrx.sample.features.dadjoke.DadJokeDetailState]
         */
        val bundle = arg?.let { Bundle().apply { putParcelable(MvRx.KEY_ARG, it) } }
        findNavController().navigate(actionId, bundle)
    }
}