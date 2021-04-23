package com.jinxian.wenshi.module_main.activity

import android.util.Log
import android.view.View.GONE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseDataBindVMActivity
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.databinding.ActivityMainBinding
import com.jinxian.wenshi.ext.infoToast
import com.jinxian.wenshi.ext.startActivity
import com.jinxian.wenshi.module_main.adapter.MainFragmentAdapter
import com.jinxian.wenshi.module_main.fragment.HomeFragment
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
        initViewPagerAndBottom()
    }

    private fun initViewPagerAndBottom() {
        mutableListOf<Fragment>().apply {
            add(HomeFragment())
            add(HomeFragment())
            add(HomeFragment())
            add(HomeFragment())
        }.also {
            mMainViewPager.adapter = MainFragmentAdapter(this, it)
        }

//        mMainViewPager.isUserInputEnabled = false
        mMainBottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> mMainViewPager.setCurrentItem(0, true)
                R.id.action_message -> mMainViewPager.setCurrentItem(1, true)
                R.id.action_discover -> mMainViewPager.setCurrentItem(2, true)
                R.id.action_mine -> mMainViewPager.setCurrentItem(3, true)
            }
            true
        }
        mMainBottomNavigation.itemIconTintList = null

    }

    override fun initData() {
        Log.i("nell-mViewModel-main1", mViewModel.toString())
        UserInfoUI.mUserModel.observe(this, Observer {
            Log.i("nell-mViewModel-main2", it.toString())
            mDataBind.mUserModel = it
            infoToast(it.toString())
            mBtnLogin.visibility = GONE;
        })
    }


}