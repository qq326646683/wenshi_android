package com.jinxian.wenshi.base.fragment

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import com.jinxian.wenshi.R
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
    }

    private fun initRefreshLayout() {
        mRefreshLayout.run {
            setOnRefreshListener(this@BaseListVMFragment)
            setOnLoadMoreListener(this@BaseListVMFragment)
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
        (mPage == 1).yes {
            mRefreshLayout.autoRefreshAnimationOnly()
        }
        getListData()
    }

    protected val mListObserver = Observer<List<M>> {
        (it != null && it.isNotEmpty()).yes {
            mMultiStateView.viewState = MultiStateView.ViewState.CONTENT
        }
        (mPage == 1).yes {
            mListData.clear()
            mListData.addAll(it)
            mRefreshLayout.finishRefresh()
        }.otherwise {
            mListData.addAll(it)
            mRefreshLayout.finishLoadMore()
        }
    }

    override fun dismissLoading() {
        mRefreshLayout.run {
            finishRefresh()
            finishLoadMore()
        }
    }

    abstract fun initRecyclerView()

    abstract fun getListData()


}