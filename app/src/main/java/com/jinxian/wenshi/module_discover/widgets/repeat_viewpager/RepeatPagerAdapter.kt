package com.jinxian.wenshi.module_discover.widgets.repeat_viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class RepeatPagerAdapter(fm: FragmentManager, datas: List<String>) : OpenPagerAdapter<String>(fm) {
    private var mDatas: MutableList<String> = mutableListOf()

    init {
        mDatas.clear()
        if (datas != null) {
            mDatas.addAll(datas)
        }
    }


    override fun getItem(position: Int): Fragment {
        return RepeatFragment.newInstance(mDatas[position])
    }

    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getItemData(position: Int): String {
        return mDatas[position]
    }

    override fun dataEquals(oldData: String?, newData: String?): Boolean {
        return oldData == newData
    }

    override fun getDataPosition(data: String?): Int {
        return mDatas.indexOf(data)
    }

    public fun getCurrentFragment(): RepeatFragment? {
        return currentPrimaryItem as? RepeatFragment
    }

    public fun setNewData(datas: List<String>) {
        mDatas.clear()
        mDatas.addAll(datas)
        notifyDataSetChanged()
    }

    public fun moveLastDataToFirst() {
        val moveData = mDatas.removeLast()
        mDatas.add(0, moveData)
        notifyDataSetChanged()
    }

    public fun moveFirstDataToLast() {
        val moveData = mDatas.removeFirst()
        mDatas.add(moveData)
        notifyDataSetChanged()
    }
}