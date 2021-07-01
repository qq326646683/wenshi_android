package com.jinxian.wenshi.module_discover.widgets.bottomsheet

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jinxian.wenshi.R

class BottomSheetDemoAdapter : BaseQuickAdapter<String, BaseViewHolder> {

    constructor(list: MutableList<String>) : super(R.layout.layout_bottomsheet_item, list)

    override fun convert(holder: BaseViewHolder, item: String) {
        val tvItem = holder.getView<TextView>(R.id.tvItem)
        tvItem.text = item
    }

}