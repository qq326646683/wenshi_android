package com.jinxian.wenshi.module_discover.widgets.popular_recyclerview

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jinxian.wenshi.R

class SimpleRecyclerViewAdapter : BaseQuickAdapter<Int, BaseViewHolder> {

    constructor(list: MutableList<Int>) : super(R.layout.layout_popular_recyclerview_item, list)

    override fun convert(holder: BaseViewHolder, item: Int) {
        val tvItem = holder.getView<TextView>(R.id.tv)
        tvItem.text = item.toString()
    }

}