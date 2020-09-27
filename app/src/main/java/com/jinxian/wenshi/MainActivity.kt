package com.jinxian.wenshi

import com.jinxian.wenshi.base.activity.BaseDataBindActivity
import com.jinxian.wenshi.databinding.ActivityMainBinding
import com.jinxian.wenshi.ext.startActivity
import com.jinxian.wenshi.module_user.activity.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        mBtnLogin.setOnClickListener {
            startActivity<LoginActivity>(false)
        }
    }

}