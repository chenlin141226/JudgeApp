package com.judge.app.fragments.judge

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.data.bean.Forum
import com.judge.data.repository.JudgeRepository
import com.judge.databinding.FragmentJudgeDetailBindingImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_judge_detail.*

class JudgeDetailViewModel : ViewModel() {

    var header = MutableLiveData<Forum>()
    var desc : String? = null


    fun onBack() {

    }

    @SuppressLint("CheckResult")
    fun getJudgeDetailModel(page:Int, fid : Int) {
        val maps = hashMapOf("page" to "$page", "fid" to "$fid")
        JudgeRepository.getNewCategoryDetail(maps).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                header.value = it.Variables.forum
                desc = it.Variables.forum.description
            }
    }

}

class JudgeDetailFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(JudgeDetailViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_judge_detail, container, false)
        val binding = DataBindingUtil.bind<FragmentJudgeDetailBindingImpl>(view)

        binding?.viewModel = viewModel
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getJudgeDetailModel(1,90)
        viewModel.header.observe(this, Observer {

        })
        val titles = arrayOf(
            resources.getString(R.string.newest),
            resources.getString(R.string.hot),
            resources.getString(R.string.hotcard),
            resources.getString(R.string.essence)
        )
        val fragments = ArrayList<Fragment>().also {
            for (index in 0 until titles.size) {
                it.add(JudgeCateGoryDetailFragment(index))
            }
        }

        viewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments, titles)
        viewPager.offscreenPageLimit = 4
        tabLayout.setViewPager(viewPager)
        viewPager.currentItem = 0
    }
}
