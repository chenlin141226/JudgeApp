package com.judge.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.judge.app.fragments.market.AllProductFragment

/**
 * @author: jaffa
 * @date: 2019/8/4
 */
@Suppress("DEPRECATION")
class MarketPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var fragments: MutableList<Fragment> = ArrayList()
    var titles: Array<String>? = null


    init {
        for (index in 0 until 4) {
            fragments.add(AllProductFragment(index))

        }
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