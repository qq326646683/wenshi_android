package com.jinxian.wenshi.module_user.api

import com.jinxian.wenshi.data.http.ResponseModel
import com.jinxian.wenshi.module_user.model.UserLoginReqModel
import com.jinxian.wenshi.module_user.model.UserModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @POST("users/login")
    suspend fun login(@Body userLoginReqModel: UserLoginReqModel): ResponseModel<UserModel>

    @GET("users/userinfo")
    suspend fun getUserInfo(): ResponseModel<UserModel>
}