package com.judge.app.fragments.judge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.*
import com.flyco.tablayout.SlidingTabLayout
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.Attention
import com.judge.data.bean.Forum
import com.judge.data.bean.Forumlist
import com.judge.data.repository.JudgeRepository
import com.judge.fragmentJudgeDetail
import com.judge.utils.LogUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.reactivex.schedulers.Schedulers

/**
 * 裁判说详情界面
 */
data class JudgeDetailState(
    val attention: Forumlist,
    val isLoading: Boolean = false,
    val forum: Forum? = null
) : MvRxState {
    constructor(args: Forumlist) : this(attention = args)
}

class JudgeDetailViewModel(initialiState: JudgeDetailState) : MvRxViewModel<JudgeDetailState>(initialiState) {


    init {
        fetchDetail()
    }

    fun fetchDetail() = withState { state ->

        if (state.isLoading) return@withState
        val map = hashMapOf("page" to "1", "fid" to state.attention.fid)
        JudgeRepository.getNewCategoryDetail(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(forum = it()?.Variables?.forum) }
    }

    fun reset(){
        setState { copy(forum = null) }
    }

    companion object : MvRxViewModelFactory<JudgeDetailViewModel, JudgeDetailState> {

        override fun create(viewModelContext: ViewModelContext, state: JudgeDetailState): JudgeDetailViewModel? {
            return JudgeDetailViewModel(state)
        }
    }
}

class JudgeDetailFragment : BaseMvRxFragment() {

    lateinit var recyclerView: EpoxyRecyclerView
    lateinit var refreshLayouts: SmartRefreshLayout
    lateinit var tableLayout: SlidingTabLayout
    lateinit var viewPager: ViewPager
    lateinit var  back : LinearLayout
    lateinit var tv_title : TextView
    val epoxyController by lazy { epoxyController() }
    val viewModel: JudgeDetailViewModel by fragmentViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.judge_base_view, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            refreshLayouts = findViewById(R.id.refreshLayout)
            tableLayout = findViewById(R.id.tabLayout)
            viewPager = findViewById(R.id.viewPager)
            back = findViewById(R.id.back)
            tv_title = findViewById(R.id.tv_title)
            back.setOnClickListener { findNavController().popBackStack() }
            val linearLayoutManager = object : LinearLayoutManager(context,RecyclerView.VERTICAL,false){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setController(epoxyController)

            initView()
            initData()
        }
    }

    private fun initView() {
        val titles = arrayOf(
            resources.getString(R.string.newest),
            resources.getString(R.string.hot),
            resources.getString(R.string.hotcard),
            resources.getString(R.string.essence)
        )
        withState(viewModel) { state ->
            tv_title.text = state.attention.name
            val fragments = ArrayList<Fragment>().also {
                it.add(JudgeCateGoryDetailFragment(state.attention.fid))
                it.add(HotCategoryDatailFragment(state.attention.fid))
                it.add(HotCardCategoryDatailFragment(state.attention.fid))
                it.add(EssenceCategoryDatailFragment(state.attention.fid))
            }
            viewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments, titles)
            viewPager.offscreenPageLimit = 4
            tableLayout.setViewPager(viewPager)
            viewPager.currentItem = 0
        }
    }

    fun initData(){
        viewModel.fetchDetail()
    }

    fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.forum?.let {
            fragmentJudgeDetail {
                id(state.forum.fid + state.forum.posts)
                forum(it)
            }
        }
    }

    override fun invalidate() {
        recyclerView.requestModelBuild()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        epoxyController.cancelPendingModelBuild()
        super.onDestroyView()
    }


}


/**
 * Create a [MvRxEpoxyController] that builds models with the given callback.
 * When models are built the current state of the viewmodel will be provided.
 */
fun <S : MvRxState, A : MvRxViewModel<S>> JudgeDetailFragment.simpleController(
    viewModel: A,
    buildModels: EpoxyController.(state: S) -> Unit
) = MvRxEpoxyController {
    if (view == null || isRemoving) return@MvRxEpoxyController
    withState(viewModel) { state ->
        buildModels(state)
    }
}

