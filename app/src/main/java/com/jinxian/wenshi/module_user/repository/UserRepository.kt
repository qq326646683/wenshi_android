package com.jinxian.wenshi.module_user.repository

import android.util.Log
import com.jinxian.wenshi.data.http.ResponseModel
import com.jinxian.wenshi.module_user.api.UserApi
import com.jinxian.wenshi.module_user.model.UserLoginReqModel
import com.jinxian.wenshi.module_user.model.UserModel

class UserRepository(private val mUserApi: UserApi) {

    suspend fun login(userLoginReqModel: UserLoginReqModel): ResponseModel<UserModel> {
        Log.i("nell-loginres1", "-----")
        val res = mUserApi.login(userLoginReqModel)
        Log.i("nell-loginres2", "--:${res}")
        return res
    }

    suspend fun getUserInfo(): ResponseModel<UserModel> = mUserApi.getUserInfo()

}