package com.jinxian.wenshi.base.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDataBindActivity<DB : ViewDataBinding> : BaseActivity() {
    lateinit var mDataBind: DB

    override fun setContentLayout() {
        mDataBind = DataBindingUtil.setContentView(this, getLayoutId())
        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mDataBind.isInitialized) {
            mDataBind.unbind()
        }
    }
}