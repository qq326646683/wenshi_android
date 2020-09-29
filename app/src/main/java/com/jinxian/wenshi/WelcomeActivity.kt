package com.jinxian.wenshi

import com.afollestad.assent.AssentResult
import com.afollestad.assent.Callback
import com.afollestad.assent.Permission
import com.jinxian.wenshi.base.activity.BaseDataBindActivity
import com.jinxian.wenshi.databinding.ActivityWelcomeBinding
import com.jinxian.wenshi.ext.*
import com.jinxian.wenshi.module_main.activity.MainActivity
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import kotlinx.coroutines.*
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
    }

    private fun askForPermission() {
        runWithPermissions(
            Permission.READ_PHONE_STATE, Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE, granted = object : Callback {
                override fun invoke(result: AssentResult) {
                    (isLogin()).yes {
                        getUserInfo()
                    }
                    startActivity<MainActivity>()
                }
            }, denied = object : Callback {
                override fun invoke(result: AssentResult) {
                    finish()
                }
            }
        )
    }

    private fun getUserInfo() {
        mViewModel.getUserInfo()
    }
}