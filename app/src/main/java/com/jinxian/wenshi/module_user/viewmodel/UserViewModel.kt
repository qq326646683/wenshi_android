package com.jinxian.wenshi.module_user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.data.http.ResponseModel
import com.jinxian.wenshi.ext.infoToast
import com.jinxian.wenshi.ext.otherwise
import com.jinxian.wenshi.ext.yes
import com.jinxian.wenshi.module_user.activity.LoginActivity
import com.jinxian.wenshi.module_user.model.UserLoginModel
import com.jinxian.wenshi.module_user.model.UserLoginReqModel
import com.jinxian.wenshi.module_user.model.UserModel
import com.jinxian.wenshi.module_user.repository.UserRepository

object UserInfoUI {
    val mUserModel = MutableLiveData<UserModel>()
}

class UserViewModel(private val mUserRepository: UserRepository) : BaseViewModel() {

    fun login(userLoginModel: UserLoginModel) {
        launch {
            val response: ResponseModel<UserModel> = mUserRepository.login(
                UserLoginReqModel(
                    userLoginModel.phone.get()!!,
                    userLoginModel.password.get()!!
                )
            )
            Log.i("nell-response", "--:$response")

            (response.status == "success").yes {
                val obj = response.obj
                Log.i("nell-obj1", "--:$obj")
                UserInfoUI.mUserModel.value = obj
            }.otherwise {
                infoToast(response.message)
            }

        }

    }

    fun getUserInfo() {
        launch {
            val obj = mUserRepository.getUserInfo().obj
            Log.i("nell-obj2", "--:$obj")
            UserInfoUI.mUserModel.value = obj
        }
    }


}

