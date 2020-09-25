package com.jinxian.wenshi.module_user.repository

import com.jinxian.wenshi.module_user.api.UserApi
import com.jinxian.wenshi.module_user.model.UserLoginModel
import com.jinxian.wenshi.module_user.model.UserModel

class UserRepository(private val mUserApi: UserApi) {

    suspend fun login(userLoginModelReq: UserLoginModel): UserModel = mUserApi.login(userLoginModelReq)

}