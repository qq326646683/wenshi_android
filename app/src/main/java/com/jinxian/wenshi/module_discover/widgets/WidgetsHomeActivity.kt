package com.jinxian.wenshi.module_discover.widgets

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import com.jinxian.wenshi.ext.startActivity
import com.jinxian.wenshi.module_discover.widgets.aite.AiteWidgetActivity
import com.jinxian.wenshi.module_discover.widgets.popular_recyclerview.PopularRecyclerViewActivity
import com.jinxian.wenshi.module_discover.widgets.repeat_viewpager.RepeatViewPagerActivity
import kotlinx.android.synthetic.main.activity_widgets_home.*

class WidgetsHomeActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_widgets_home

    override fun initView() {
        llAite.setOnClickListener {
            startActivity<AiteWidgetActivity>()
        }

        llRepeatViewPager.setOnClickListener {
            startActivity<RepeatViewPagerActivity>()
        }


        llPopularRv.setOnClickListener {
            startActivity<PopularRecyclerViewActivity>()
        }
    }

}