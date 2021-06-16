package com.jinxian.wenshi

import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.afollestad.assent.Permission
import com.jinxian.wenshi.base.activity.BaseDataBindActivity
import com.jinxian.wenshi.databinding.ActivityWelcomeBinding
import com.jinxian.wenshi.ext.*
import com.jinxian.wenshi.media.FFMediaPlayer
import com.jinxian.wenshi.module_main.activity.MainActivity
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import com.jinxian.wenshi.util.CommonUtils
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : BaseDataBindActivity<ActivityWelcomeBinding>() {

    private val mViewModel: UserViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_welcome

    override fun initView() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000L)
            askForPermission()
        }

    }

    override fun initData() {
        mDataBind.versionName = getVersionName()
        ffmpeg.text = FFMediaPlayer.native_GetFFmpegVersion()
    }

    private fun askForPermission() {
        lifecycleScope.launch {
            runWithPermissions(
                Permission.READ_PHONE_STATE, Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE
            ).yes {
                getUserInfo()
                CommonUtils.copyAssetsDirToSDCard(this@WelcomeActivity, "byteflow", filesDir.absolutePath)
                startActivity<MainActivity>()
            }.otherwise {
                finish()
            }
        }
    }

    private fun getUserInfo() {
        mViewModel.getUserInfo()
    }
}