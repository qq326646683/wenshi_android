package com.jinxian.wenshi.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.base.viewmodel.ErrorState
import com.jinxian.wenshi.base.viewmodel.LoadState
import com.jinxian.wenshi.base.viewmodel.SuccessState
import com.jinxian.wenshi.ext.errorToast

abstract class BaseVMActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout()
    }

    open fun setContentLayout() {
        setContentView(getLayoutId())
        initViewModelAction()
        initView()
        initData()
    }

    abstract fun getLayoutId(): Int

    protected  fun initViewModelAction () {
        getViewModel().let { baseViewModel ->
            baseViewModel.mStateLiveData.observe(this, Observer {
                when (it) {
                    LoadState -> showLoading()
                    SuccessState -> dismissLoading()
                    is ErrorState -> {
                        dismissLoading()
                        it.message?.apply {
                            errorToast(this)
                            handleError()
                        }
                    }
                }
            })
        }
    }

    abstract fun initView()

    open fun initData() {

    }

    open fun showLoading() {

    }

    open fun dismissLoading() {

    }

    open fun handleError() {

    }

    abstract fun getViewModel(): BaseViewModel
}