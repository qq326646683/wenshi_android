package com.jinxian.wenshi.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDataBindVMFragment<DB : ViewDataBinding> : BaseVMFragment() {

    lateinit var mDataBind: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mDataBind = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
            //
            mDataBind.lifecycleOwner = this
            mRootView = mDataBind.root
        }
        return mRootView
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mDataBind.isInitialized) {
            mDataBind.unbind()
        }
    }

}