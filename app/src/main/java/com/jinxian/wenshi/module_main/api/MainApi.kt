package com.jinxian.wenshi.module_main.api

import com.jinxian.wenshi.data.http.ResponseModel
import com.jinxian.wenshi.module_main.model.TalkModel
import com.jinxian.wenshi.module_user.model.UserLoginReqModel
import com.jinxian.wenshi.module_user.model.UserModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MainApi {
    //http://localhost:3000/talk?limit=10&offset=0&userId=59114399&mediaType=video
    @GET("talk")
    suspend fun getTalkList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("userId") userId: String?,
        @Query("mediaType") mediaType: String
    ): ResponseModel<TalkModel>
}