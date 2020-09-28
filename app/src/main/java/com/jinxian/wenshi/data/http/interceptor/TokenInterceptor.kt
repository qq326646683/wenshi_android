package com.jinxian.wenshi.data.http.interceptor

import com.jinxian.wenshi.config.Settings
import com.jinxian.wenshi.constant.Constant
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return chain.proceed(request.newBuilder().apply {
            val token = "Bearer ${Settings.Account.token}"
            addHeader(Constant.AUTHORIZATION, token)
        }.build())
    }

}