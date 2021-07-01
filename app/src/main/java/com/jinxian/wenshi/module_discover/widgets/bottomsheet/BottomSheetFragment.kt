package com.jinxian.wenshi.module_discover.widgets.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinxian.wenshi.R
import com.jinxian.wenshi.ext.dp2px
import com.jinxian.wenshi.ext.res2Drawable
import com.jinxian.wenshi.ext.setCornerRadii
import kotlinx.android.synthetic.main.fragment_bottomsheet.*

class BottomSheetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_bottomsheet, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initBottomSheet()

        clTitle.setCornerRadii(Color.WHITE, leftTop = 15.dp2px, rightTop = 15.dp2px)
    }

    private fun initBottomSheet() {
        val behavior = BottomSheetBehavior.from(llContainerBehavior)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                ivArrow.setImageDrawable((if (newState == BottomSheetBehavior.STATE_EXPANDED) R.mipmap.ic_can_down else R.mipmap.ic_can_up).res2Drawable())
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })
    }

    private fun initRecyclerView() {
        val adapter = BottomSheetDemoAdapter(mutableListOf())
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setNewInstance(MutableList(20) { it.toString() })

    }

}