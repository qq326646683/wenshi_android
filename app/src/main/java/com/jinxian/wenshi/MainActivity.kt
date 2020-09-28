package com.jinxian.wenshi

import androidx.lifecycle.ViewModelProvider
import com.jinxian.wenshi.base.activity.BaseDataBindVMActivity
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.databinding.ActivityMainBinding
import com.jinxian.wenshi.ext.startActivity
import com.jinxian.wenshi.module_user.activity.LoginActivity
import com.jinxian.wenshi.module_user.viewmodel.UserInfoUI
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseDataBindVMActivity<ActivityMainBinding>() {

    private val mViewModel: UserViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getViewModel(): BaseViewModel = mViewModel


    override fun initView() {
        mBtnLogin.setOnClickListener {
            startActivity<LoginActivity>(false)
        }
    }

    override fun initData() {
        mDataBind.mUserModel = UserInfoUI.mUserModel.value
    }


}