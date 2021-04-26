package com.jinxian.wenshi.module_main.adapter


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Environment
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jinxian.wenshi.R
import com.jinxian.wenshi.databinding.LayoutHomeTabItemBinding
import com.jinxian.wenshi.module_main.model.TalkModel
import com.jinxian.wenshi.module_main.viewmodel.HomeViewModel
import com.tencent.qgame.animplayer.AnimView
import com.tencent.qgame.animplayer.util.ScaleType
import java.io.File

class HomeTabListAdapter(viewModel: HomeViewModel) :
    BaseQuickAdapter<TalkModel, BaseDataBindingHolder<LayoutHomeTabItemBinding>>(R.layout.layout_home_tab_item) {
    val TAG = "ListNormalAdapter22"
    private val mVideoAnimPath =
        Environment.getExternalStorageDirectory().absolutePath + "/byteflow/anim_video.mp4"
    val mViewModel = viewModel


    override fun convert(holder: BaseDataBindingHolder<LayoutHomeTabItemBinding>, item: TalkModel) {
        val dataBinding = holder.dataBinding
        dataBinding?.let {
            it.homeTabItem = item
            it.gsyVideoPlayer.loadCoverImage(item.subUrl, R.mipmap.ic_favicon)
            it.gsyVideoPlayer.setUpLazy(item.mediaUrl, true, null, null, "")
            it.gsyVideoPlayer.playTag = TAG
            it.gsyVideoPlayer.isLockLand = true
            it.gsyVideoPlayer.playPosition = holder.bindingAdapterPosition
            it.gsyVideoPlayer.backButton.visibility = View.GONE
            it.gsyVideoPlayer.setIsTouchWiget(false)

            it.ivLike.setOnClickListener { its ->
                it.ivLike.imageTintList = ColorStateList.valueOf(Color.RED)
                it.tvLikeCount.text = item.likeCount++.toString()
                playVideoAnim(it.videoAnimView)
            }
            initVideoAnimPlayer(it.videoAnimView)
        }
    }

    private fun initVideoAnimPlayer(videoAnimView: AnimView) {
        videoAnimView.setScaleType(ScaleType.FIT_CENTER)
    }

    private fun playVideoAnim(videoAnimView: AnimView) {
        videoAnimView.startPlay(File(mVideoAnimPath))
    }


}