package com.judge.adapters

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.judge.R
import com.judge.app.fragments.PublicMessageFragment
import com.judge.app.fragments.PublishedTopicFragment
import com.judge.app.fragments.market.AllProductFragment
import com.vondear.rxtool.RxTool

/**
 * @author: jaffa
 * @date: 2019/8/4
 */
@Suppress("DEPRECATION")
class MarketPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var fragments: MutableList<Fragment> = ArrayList()
    var titles: Array<String>? = null

//    val titles = arrayOf(
//        RxTool.getContext().getString(R.string.virtualCard),
//        RxTool.getContext().getString(R.string.videoMembers),
//        RxTool.getContext().getString(R.string.technology),
//        RxTool.getContext().getString(R.string.gameExtensions)
//    )

    init {
        //for (index in 0 until 4){
        fragments.add(AllProductFragment())
        fragments.add(PublishedTopicFragment())
        fragments.add(PublicMessageFragment())
        fragments.add(PublicMessageFragment())
        //}
    }


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size


    override fun getPageTitle(position: Int): CharSequence? {
        return titles!![position]
    }

    fun setTitle(titles: Array<String>) {
        this.titles = titles
    }

}