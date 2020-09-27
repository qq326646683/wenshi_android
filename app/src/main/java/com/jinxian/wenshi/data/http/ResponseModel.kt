package com.jinxian.wenshi.data.http

class ResponseModel<T>(
    val obj: T,
    val objs: List<T>,
    val status: String,
    val message: String
)