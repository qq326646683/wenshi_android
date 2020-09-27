package com.jinxian.wenshi.module_user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.data.http.ResponseModel
import com.jinxian.wenshi.module_user.model.UserLoginModel
import com.jinxian.wenshi.module_user.model.UserLoginReqModel
import com.jinxian.wenshi.module_user.model.UserModel
import com.jinxian.wenshi.module_user.repository.UserRepository

class UserViewModel(private val mUserRepository: UserRepository) : BaseViewModel() {

    val mUserModel = MutableLiveData<UserModel>()

    fun login(userLoginModel: UserLoginModel): LiveData<ResponseModel<UserModel>> = emit {
        mUserRepository.login(
            UserLoginReqModel(
                userLoginModel.phone.get()!!,
                userLoginModel.password.get()!!
            )
        )

    }

    fun getUserInfo() {
        launch {
            mUserModel.value = mUserRepository.getUserInfo().obj
        }
    }

}