package com.jinxian.wenshi.module_main.repository

import com.jinxian.wenshi.data.http.ResponseModel
import com.jinxian.wenshi.module_main.api.MainApi
import com.jinxian.wenshi.module_main.model.TalkModel

class MainRepository(private val mMainApi: MainApi) {

    suspend fun getTalkList(
        limit: Int,
        offset: Int,
        userId: String,
        mediaType: String
    ): ResponseModel<TalkModel> = mMainApi.getTalkList(limit, offset, userId, mediaType)

}