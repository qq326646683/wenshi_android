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
import com.jinxian.wenshi.module_main.fragment.DiscoverFragment
import com.jinxian.wenshi.module_main.fragment.HomeFragment
import com.jinxian.wenshi.module_main.fragment.MessageFragment
import com.jinxian.wenshi.module_main.fragment.MineFragment
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
        initViewPagerAndBottom()
    }

    private fun initViewPagerAndBottom() {
        mutableListOf<Fragment>().apply {
            add(HomeFragment())
            add(MessageFragment())
            add(DiscoverFragment())
            add(MineFragment())
        }.also {
            mMainViewPager.adapter = MainFragmentAdapter(this, it)
        }

        mMainViewPager.isUserInputEnabled = false
        mMainBottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> mMainViewPager.setCurrentItem(0, false)
                R.id.action_message -> mMainViewPager.setCurrentItem(1, false)
                R.id.action_discover -> mMainViewPager.setCurrentItem(2, false)
                R.id.action_mine -> mMainViewPager.setCurrentItem(3, false)
            }
            true
        }
        mMainBottomNavigation.itemIconTintList = null

    }

    override fun initData() {

    }


}