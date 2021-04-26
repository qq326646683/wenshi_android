package com.jinxian.wenshi.module_main.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jinxian.wenshi.base.fragment.BaseListVMFragment
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.databinding.LayoutHomeTabItemBinding
import com.jinxian.wenshi.module_main.adapter.HomeTabListAdapter
import com.jinxian.wenshi.module_main.model.TalkModel
import com.jinxian.wenshi.module_main.viewmodel.HomeViewModel
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.common_refresh_recyclerview.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseListVMFragment<TalkModel>() {

    private val mViewModel: HomeViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = mViewModel

    override fun initRecyclerView() {

        val snapHelper: PagerSnapHelper = object : PagerSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager?,
                velocityX: Int,
                velocityY: Int
            ): Int {
                val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
                if (position >= 0) {
                    GSYVideoManager.releaseAllVideos()
                    playPosition(position)
                }
                return position
            }
        }

        mRecyclerView.let {
            it.adapter = HomeTabListAdapter(mViewModel).apply {
                setNewInstance(mListData)
            }
            it.layoutManager = LinearLayoutManager(mActivity)
            it.onFlingListener = null
            snapHelper.attachToRecyclerView(it)
        }

        mRecyclerView.postDelayed(Runnable {
            if (mListData.size > 0) {
                playPosition(0)
            }
        }, 100)

    }

    fun playPosition(position: Int) {
        mRecyclerView?.findViewHolderForAdapterPosition(position)?.let {
            (it as BaseDataBindingHolder<LayoutHomeTabItemBinding>).dataBinding?.gsyVideoPlayer?.startPlayLogic()
        }
    }

    override fun initData() {
        getListData()
    }

    override fun getListData() {
        mViewModel.getHomeData(mPage).observe(this, mListObserver)
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

}