package com.situm.capacitycontroltestsuite.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class DebugTabAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val mTabsMaps = Hashtable<Int, Tab>()

    override fun getItem(p0: Int): Fragment = mTabsMaps[p0]!!.fragment

    override fun getCount() = mTabsMaps.size

    fun addTab(index: Int, tab: Tab) {
        mTabsMaps[index] = tab
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTabsMaps[position]?.title
    }

    data class Tab(val title: String, val fragment: Fragment)
}