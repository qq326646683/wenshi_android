package com.jinxian.wenshi.base.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDataBindVMActivity<DB : ViewDataBinding> : BaseVMActivity() {

    lateinit var mDataBind: DB

    override fun setContentLayout() {
        mDataBind = DataBindingUtil.setContentView(this, getLayoutId())
        mDataBind.lifecycleOwner = this
        initViewModelAction()
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