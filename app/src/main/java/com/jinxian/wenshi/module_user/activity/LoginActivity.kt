package com.jinxian.wenshi.module_user.activity

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseDataBindVMActivity
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.databinding.ActivityLoginBinding
import com.jinxian.wenshi.ext.infoToast
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseDataBindVMActivity<ActivityLoginBinding>() {

    private val mViewModel: UserViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        mLogin.setOnClickListener {
            infoToast("登录")
        }
    }

    override fun getViewModel(): BaseViewModel = mViewModel
}