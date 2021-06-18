package com.jinxian.wenshi.util

interface DownloadListener {
    fun callBack(taskId: Int, status: Int, progress: Int)
}