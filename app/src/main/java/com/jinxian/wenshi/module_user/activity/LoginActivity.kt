package com.jinxian.wenshi.module_user.activity

import androidx.lifecycle.Observer
import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseDataBindVMActivity
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.config.Settings
import com.jinxian.wenshi.databinding.ActivityLoginBinding
import com.jinxian.wenshi.ext.otherwise
import com.jinxian.wenshi.ext.yes
import com.jinxian.wenshi.module_user.model.UserLoginModel
import com.jinxian.wenshi.module_user.viewmodel.UserInfoUI
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseDataBindVMActivity<ActivityLoginBinding>() {
    private val mViewModel: UserViewModel by viewModel()

    private val mUserLoginModel: UserLoginModel by lazy { UserLoginModel() }

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun getViewModel(): BaseViewModel = mViewModel

    override fun initView() {
        mLogin.setOnClickListener {
            clickLogin()
        }
    }

    override fun initData() {
        mDataBind.userLoginModel = mUserLoginModel

    }

    private fun clickLogin() {
        val phone = mUserLoginModel.phone.get().toString()
        val password = mUserLoginModel.password.get().toString()

        phone.isEmpty().yes {
            mPhoneInputLayout.error = getString(R.string.phone_not_null)
            mPhoneInputLayout.isErrorEnabled = true
        }.otherwise {
            mPhoneInputLayout.isErrorEnabled = false
            password.isEmpty().yes {
                mPasswordInputLayout.error = getString(R.string.password_not_null)
                mPasswordInputLayout.isErrorEnabled = true
            }.otherwise {
                mPasswordInputLayout.isErrorEnabled = false
                login()
            }
        }
    }

    private fun login() {
        mViewModel.login(mUserLoginModel)
        UserInfoUI.mUserModel.observe(this, Observer {
            Settings.Account.token = it.token
            finish()
        })
    }


}