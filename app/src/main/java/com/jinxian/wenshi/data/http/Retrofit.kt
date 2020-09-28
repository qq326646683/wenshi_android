package com.jinxian.wenshi.data.http

import android.util.Log
import com.jinxian.wenshi.BuildConfig
import com.jinxian.wenshi.data.http.interceptor.TokenInterceptor
import com.jinxian.wenshi.module_user.api.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://172.16.0.104:3000"
private const val TIMT_OUT = 60L
private const val TAG = "wenshi"

val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Log.e(TAG, message)
    }
}).also {
    it.level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(TokenInterceptor())
    .connectTimeout(TIMT_OUT, TimeUnit.SECONDS)
    .writeTimeout(TIMT_OUT, TimeUnit.SECONDS)
    .readTimeout(TIMT_OUT, TimeUnit.SECONDS)
    .also {
        if (BuildConfig.DEBUG) {
            it.addInterceptor(httpLoggingInterceptor)
        }
    }.build()



val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .build()

object UserService : UserApi by retrofit.create(UserApi::class.java)