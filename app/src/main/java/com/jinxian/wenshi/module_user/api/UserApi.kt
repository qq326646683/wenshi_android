package com.jinxian.wenshi.module_user.api

import com.jinxian.wenshi.module_user.model.UserLoginModel
import com.jinxian.wenshi.module_user.model.UserModel
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("users/login")
    suspend fun login(@Body userLoginModelReq: UserLoginModel): UserModel
}