package com.jinxian.wenshi.module_discover.widgets.popular_recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RVItemExposureListener(
    private val mRecyclerView: RecyclerView,
    private val mExposureListener: IOnExposureListener?
) {
    interface IOnExposureListener {
        fun onExposure(position: Int)
        fun onUpload(exposureList: List<Int>?): Boolean
    }

    private val mExposureList: MutableList<Int> = ArrayList()
    private val mUploadList: MutableList<Int> = ArrayList()
    private var mScrollState = 0

    var isEnableExposure = true
    private var mCheckChildViewExposure = true

    private val mViewVisible = Rect()
    fun checkChildExposeStatus() {
        if (!isEnableExposure) {
            return
        }
        val length = mRecyclerView.childCount
        if (length != 0) {
            var view: View?
            for (i in 0 until length) {
                view = mRecyclerView.getChildAt(i)
                if (view != null) {
                    view.getLocalVisibleRect(mViewVisible)
                    if (mViewVisible.height() > view.height / 2 && mViewVisible.top < mRecyclerView.bottom) {
                        checkExposure(view)
                    }
                }
            }
        }
    }

    private fun checkExposure(childView: View): Boolean {
        val position = mRecyclerView.getChildAdapterPosition(childView)
        if (position < 0 || mExposureList.contains(position)) {
            return false
        }
        mExposureList.add(position)
        mUploadList.add(position)
        mExposureListener?.onExposure(position)
        return true
    }

    private fun uploadList() {
        if (mScrollState == RecyclerView.SCROLL_STATE_IDLE && mUploadList.size > 0 && mExposureListener != null) {
            val success = mExposureListener.onUpload(mUploadList)
            if (success) {
                mUploadList.clear()
            }
        }
    }

    fun resetExposureData() {
        mExposureList.clear()
        mUploadList.clear()
        mCheckChildViewExposure = true
    }

    fun refreshFinish() {
        mCheckChildViewExposure = true
    }

    fun setOffsetVertical(offsetVertical: Int) {
        if (offsetVertical != 0) {
            checkChildExposeStatus()
        }
    }

    init {
        mRecyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            if (mRecyclerView.childCount == 0 || !mCheckChildViewExposure) {
                return@addOnGlobalLayoutListener
            }
            checkChildExposeStatus()
            uploadList()
            mCheckChildViewExposure = false
        }
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                mScrollState = newState
                uploadList()
            }

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isEnableExposure) {
                    return
                }

                // 大于50视为滑动过快
                if (mScrollState == RecyclerView.SCROLL_STATE_SETTLING && Math.abs(dy) > 50) {
                    return
                }
                checkChildExposeStatus()
            }
        })
    }
}