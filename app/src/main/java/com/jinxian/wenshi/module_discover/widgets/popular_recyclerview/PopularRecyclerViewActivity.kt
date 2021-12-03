package com.jinxian.wenshi.module_discover.widgets.popular_recyclerview

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_popular_recyclerview.*

class PopularRecyclerViewActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_popular_recyclerview

    override fun initView() {
        val simpleRecyclerViewAdapter = SimpleRecyclerViewAdapter((0..100).toMutableList())
        popularRv.adapter = simpleRecyclerViewAdapter
        popularRv.layoutManager = LinearLayoutManager(this)

        RVItemExposureListener(popularRv, object : RVItemExposureListener.IOnExposureListener {
            override fun onExposure(position: Int) {
                Log.d("exposure-curPosition:", position.toString())
            }

            override fun onUpload(exposureList: List<Int>?): Boolean {
                Log.d("exposure-positionList", exposureList.toString())
                // 上报成功后返回true
                return true
            }

        })
    }

}