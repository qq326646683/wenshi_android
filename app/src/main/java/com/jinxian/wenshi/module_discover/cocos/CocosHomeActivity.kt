package com.jinxian.wenshi.module_discover.cocos

import android.Manifest
import android.app.DownloadManager
import android.content.Intent
import android.os.Environment
import android.util.Log
import com.cocos.lib.GlobalObject
import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import com.jinxian.wenshi.util.DownloadListener
import com.jinxian.wenshi.util.DownloadUtil
import com.jinxian.wenshi.util.FileUtil
import kotlinx.android.synthetic.main.activity_cocos_home.*

class CocosHomeActivity : BaseActivity() {
    val download1Url = "http://file.jinxianyun.com/default.zip"

    override fun getLayoutId() = R.layout.activity_cocos_home

    override fun initView() {
        DownloadUtil.instance.addListener(listener)
        btnDownload.setOnClickListener {
            DownloadUtil.instance.download(download1Url)
        }
    }

    val listener: DownloadListener = object : DownloadListener {
        override fun callBack(taskId: Int, status: Int, progress: Int) {
            Log.i("nell--", "taskId:$taskId, status:$status, progress:$progress")
            if (DownloadUtil.instance.taskMap.get(download1Url) == taskId) {
                when (status) {
                    DownloadManager.STATUS_RUNNING -> {
                        btnDownload?.text = "下载中${progress}%"
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        btnDownload?.text = "解压中"
                        val zipFileString =
                            "${getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath}/${
                                download1Url.split("/").last()
                            }"
                        val outPathString =
                            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
                        FileUtil.unzip(
                            zipFileString,
                            outPathString,
                            object : FileUtil.ZipProgress {
                                override fun onProgress(progress: Int) {
                                    btnDownload?.text = "解压中$progress"
                                }

                                override fun onDone() {
                                    btnDownload?.text = "打开游戏"
                                    btnDownload?.setOnClickListener {
                                        val intent = Intent(
                                            this@CocosHomeActivity,
                                            CocosGameActivity::class.java
                                        )
                                        intent.putExtra(
                                            "path",
                                            "${outPathString}/${
                                                download1Url.split("/").last().split(".").first()
                                            }"
                                        )
                                        startActivity(intent)
                                    }
                                }
                            }
                        )
                    }
                    DownloadManager.STATUS_FAILED -> {
                        btnDownload?.text = "下载失败"
                    }
                }
            }
        }
    }
}