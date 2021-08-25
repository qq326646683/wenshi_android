package com.jinxian.wenshi.util

import java.util.*
import kotlin.concurrent.timerTask

object FunctionUtil {
    const val DEFAULT_DURATION_TIME = 300L
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
     * @duration 非必传，默认300ms
     * @continueCall 非必传，用来连续调用时提示用户"不要快速点击"
     * @doThing 必传
     */
    private val lastTimeMillMap = mutableMapOf<Int, Long>()
    fun throttle(
        duration: Long = DEFAULT_DURATION_TIME,
        continueCall: (() -> Unit)? = null,
        doThing: () -> Unit
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - (lastTimeMillMap[doThing.hashCode()] ?: 0L) > duration) {
            doThing.invoke()
            lastTimeMillMap[doThing.hashCode()] = System.currentTimeMillis()
        } else {
            continueCall?.invoke()
        }
    }
}