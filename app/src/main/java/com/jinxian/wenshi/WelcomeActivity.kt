package com.jinxian.wenshi

import com.afollestad.assent.AssentResult
import com.afollestad.assent.Callback
import com.afollestad.assent.Permission
import com.jinxian.wenshi.base.activity.BaseDataBindActivity
import com.jinxian.wenshi.databinding.ActivityWelcomeBinding
import com.jinxian.wenshi.ext.getVersionName
import com.jinxian.wenshi.ext.runWithPermissions
import com.jinxian.wenshi.ext.startActivity

class WelcomeActivity : BaseDataBindActivity<ActivityWelcomeBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_welcome

    override fun initView() {
        askForPermission()
    }

    override fun initData() {
        mDataBind.versionName = getVersionName()
    }

    private fun askForPermission() {
        runWithPermissions(
            Permission.READ_PHONE_STATE, Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE, granted = object : Callback {
                override fun invoke(result: AssentResult) {
                    getUserInfo()
                }
            }, denied = object : Callback {
                override fun invoke(result: AssentResult) {
                    finish()
                }
            }
        )
    }

    private fun getUserInfo() {
        // todo 检查本地token
        startActivity<MainActivity>()
    }
}