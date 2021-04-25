package com.jinxian.wenshi.util

import java.util.*
import kotlin.concurrent.timerTask

object FunctionUtil {
    private const val DEFAULT_DURATION_TIME = 300L
    var timer: Timer? = null


    /**
     * 防抖函数: eg:输入框连续输入，用户停止操作300ms才执行访问接口
     */
    fun antiShake(duration: Long = DEFAULT_DURATION_TIME, doThing: () -> Unit) {
        timer?.cancel()
        timer = Timer().apply {
            schedule(timerTask {
                doThing.invoke()
                timer = null
            }, duration)
        }
    }

    /**
     * 节流函数: eg:300ms内，只会触发一次
     */
    var lastTimeMill = 0L
    fun throttle(duration: Long = DEFAULT_DURATION_TIME, continueCall: (() -> Unit)? = null, doThing: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTimeMill > duration) {
            doThing.invoke()
            lastTimeMill = System.currentTimeMillis()
        } else {
            continueCall?.invoke()
        }
    }
}