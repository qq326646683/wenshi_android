package com.jinxian.wenshi.base.fragment

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import com.jinxian.wenshi.R
import com.jinxian.wenshi.constant.Constant
import com.jinxian.wenshi.ext.otherwise
import com.jinxian.wenshi.ext.yes
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.common_refresh_recyclerview.*

abstract class BaseListVMFragment<M> : BaseVMFragment(), OnRefreshListener, OnLoadMoreListener {

    protected val mListData = ObservableArrayList<M>()

    protected var mPage = 1

    override fun getLayoutRes(): Int = R.layout.common_refresh_recyclerview

    override fun initView() {
        initRefreshLayout()
        initRecyclerView()
        initMultiStateView()
    }

    private fun initRefreshLayout() {
        mRefreshLayout.run {
            setOnRefreshListener(this@BaseListVMFragment)
            setOnLoadMoreListener(this@BaseListVMFragment)
        }
    }

    private fun initMultiStateView() {
        mMultiStateView.viewState = MultiStateView.ViewState.LOADING

        mMultiStateView.getView(MultiStateView.ViewState.ERROR)?.setOnClickListener {
            mPage = 1
            mMultiStateView.viewState = MultiStateView.ViewState.LOADING
            initViewModelAction()
        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPage = 1
        initViewModelAction()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mPage ++
        initViewModelAction()
    }

    protected fun initViewModelAction() {
        getListData()
    }

    protected val mListObserver = Observer<List<M>> {
        Log.i("nell-mListObserver", "--${it}")

        (it != null && it.isEmpty()).yes {
            mMultiStateView.viewState = MultiStateView.ViewState.EMPTY
        }

        (it != null && it.isNotEmpty()).yes {
            mMultiStateView.viewState = MultiStateView.ViewState.CONTENT
        }



        (mPage == 1).yes {
            mListData.clear()
            mListData.addAll(it)
            (it.size < Constant.PAGE_SIZE).yes {
                mRefreshLayout.finishRefreshWithNoMoreData()
            }.otherwise {
                mRefreshLayout.finishRefresh()
            }
        }.otherwise {
            mListData.addAll(it)
            (it.size < Constant.PAGE_SIZE).yes {
                mRefreshLayout.finishLoadMoreWithNoMoreData()
            }.otherwise {
                mRefreshLayout.finishLoadMore()
            }
        }
    }


    override fun showSuccess() {
        mRefreshLayout.run {
            finishRefresh()
            finishLoadMore()
        }
    }

    override fun showError() {
        mMultiStateView.viewState = MultiStateView.ViewState.ERROR
        mRefreshLayout.run {
            finishRefresh()
            finishLoadMore()
        }
    }

    abstract fun initRecyclerView()

    abstract fun getListData()


}