package com.jinxian.wenshi.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.cocos.lib.GlobalObject
import com.jinxian.wenshi.App
import com.jinxian.wenshi.AppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

class DownloadUtil {
    val downloadManager: DownloadManager = AppContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    var taskMap: MutableMap<String, Int> = mutableMapOf()
    val downloadListenerList: MutableList<DownloadListener> = mutableListOf()

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadUtil() }
    }

    init {
        Timer().schedule(timerTask {
            getDownloadInfo()
        }, Date(), 1000)
    }

    fun download(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        //设置漫游条件下是否可以下载
        request.setAllowedOverRoaming(true)
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)


        request.setDestinationInExternalFilesDir(AppContext, Environment.DIRECTORY_DOWNLOADS, url.split("/").last())
        val taskId = downloadManager.enqueue(request)
        taskMap[url] = taskId.toInt()
    }

    fun addListener(listener: DownloadListener) {
        downloadListenerList.add(listener)
    }

    fun removeListener(listener: DownloadListener) {
        downloadListenerList.remove(listener)
    }


    var curTaskId: Int = 0
    var curStatus: Int = 0
    var curProgress: Int = 0
    private fun getDownloadInfo() {
        val query = DownloadManager.Query()
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val taskId = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

            val downloadBytes: Long = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val totalBytes: Long = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            val progress = (downloadBytes * 100 / totalBytes).toInt()

            if (taskId == curTaskId && status == curStatus && progress == curProgress) return

            GlobalScope.launch(Dispatchers.Main) {
                downloadListenerList.forEach {
                    it.callBack(taskId, status, progress)
                }
            }


            curTaskId = taskId
            curStatus = status
            curProgress = progress
        }
    }

}