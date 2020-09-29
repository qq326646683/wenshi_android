package com.jinxian.wenshi.module_main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainFragmentAdapter(fragmentActivity: FragmentActivity, private val mFragmentList: List<Fragment>) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = mFragmentList.size

    override fun createFragment(position: Int): Fragment = mFragmentList[position]

}