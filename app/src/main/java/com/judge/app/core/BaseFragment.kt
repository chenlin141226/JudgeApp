package com.judge.app.core


import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import com.judge.R
import com.judge.app.activities.HomeActivity
import com.judge.models.SharedViewModel
import com.judge.utils.CenterTitle.centerTitle
import com.judge.utils.LogUtils
import com.judge.utils.NetworkUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.activity_home.*

abstract class BaseFragment : BaseMvRxFragment() {

    protected lateinit var recyclerView: EpoxyRecyclerView
    protected lateinit var toolbar: Toolbar
    protected lateinit var refreshLayout: SmartRefreshLayout
    protected lateinit var sharedViewModel: SharedViewModel
    protected lateinit var rightButton: Button
    protected lateinit var titleViewStub: ViewStub
    protected lateinit var bottomViewStub: ViewStub
    //protected lateinit var coordinatorLayout: CoordinatorLayout
    protected val epoxyController by lazy { epoxyController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        sharedViewModel.visible.observe(this, Observer {
            try {
                if (it) {
                    (activity as HomeActivity).bottom_nav.visibility = View.VISIBLE
                    (activity as HomeActivity).signImageView.visibility = View.VISIBLE
                } else {
                    (activity as HomeActivity).bottom_nav.visibility = View.GONE
                    (activity as HomeActivity).signImageView.visibility = View.GONE
                }
            } catch (e: Exception) {
                LogUtils.e(e.toString())
            }
        })
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
            rightButton = findViewById(R.id.right_button)
            titleViewStub = findViewById(R.id.titleStub)
            bottomViewStub = findViewById(R.id.bottomStub)
            // coordinatorLayout = findViewById(R.id.coordinator_layout)
            recyclerView.setController(epoxyController)
            initView()
            initData()
            setToolBar()
            toolbar.setupWithNavController(findNavController())
            centerTitle(toolbar, true)
        }
    }

    open fun setToolBar() {

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

    open fun initView() {}

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