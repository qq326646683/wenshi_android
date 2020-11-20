package com.jinxian.wenshi.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.base.viewmodel.ErrorState
import com.jinxian.wenshi.base.viewmodel.LoadState
import com.jinxian.wenshi.base.viewmodel.SuccessState
import com.jinxian.wenshi.ext.errorToast

abstract class BaseVMFragment : Fragment() {

    protected var mRootView: View? = null

    private var mIsHasData = false //是否加载过数据

    lateinit var mActivity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelAction()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = View.inflate(mActivity, getLayoutRes(), null)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        layzyLoadData()
    }

    private fun layzyLoadData() {
        if (!mIsHasData) {
            mIsHasData = true
            initData()
        }
    }

    private fun initViewModelAction() {
        getViewModel().let { baseViewModel ->
            baseViewModel.mStateLiveData.observe(this, Observer {
                when (it) {
                    LoadState -> showLoading()
                    SuccessState -> showSuccess()
                    is ErrorState -> {
                        showError()
                        it.message?.apply {
                            errorToast(this)
                            handleError()
                        }
                    }
                }
            })
        }
    }

    abstract fun getLayoutRes(): Int

    abstract fun getViewModel(): BaseViewModel

    abstract fun initView()

    open fun initData() {

    }

    open fun showLoading() {

    }

    open fun showSuccess() {

    }

    open fun showError() {

    }

    open fun handleError() {

    }

}