package com.jinxian.wenshi.module_main.fragment

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.fragment.BaseDataBindVMFragment
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.databinding.FragmentMineBinding
import com.jinxian.wenshi.ext.infoToast
import com.jinxian.wenshi.ext.startActivity
import com.jinxian.wenshi.module_user.activity.LoginActivity
import com.jinxian.wenshi.module_user.viewmodel.UserInfoUI
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_mine.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MineFragment : BaseDataBindVMFragment<FragmentMineBinding>() {
    private val mViewModel: UserViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.fragment_mine

    override fun getViewModel(): BaseViewModel = mViewModel

    override fun initView() {
        mBtnLogin.setOnClickListener {
            activity?.startActivity<LoginActivity>(false)
        }
    }

    override fun initData() {
        UserInfoUI.mUserModel.observe(this, Observer {
            Log.i("nell-mViewModel-minefra", "-----")
            mDataBind.mUserModel = it
            mBtnLogin.visibility = if (it == null) View.VISIBLE else View.GONE;
        })
    }
}