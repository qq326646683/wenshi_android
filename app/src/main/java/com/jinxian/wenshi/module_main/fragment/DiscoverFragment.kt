package com.jinxian.wenshi.module_main.fragment

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.fragment.BaseDataBindVMFragment
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.databinding.FragmentDiscoverBinding
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverFragment : BaseDataBindVMFragment<FragmentDiscoverBinding>() {
    private val mViewModel: UserViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.fragment_discover

    override fun getViewModel(): BaseViewModel = mViewModel

    override fun initView() {

    }
}