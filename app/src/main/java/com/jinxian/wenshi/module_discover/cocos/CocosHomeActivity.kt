package com.jinxian.wenshi.module_discover.cocos

import android.app.DownloadManager
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.cocos.bridge.CocosBridgeHelper
import com.cocos.bridge.CocosDataListener
import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import com.jinxian.wenshi.util.DownloadListener
import com.jinxian.wenshi.util.DownloadUtil
import com.jinxian.wenshi.util.FileUtil
import kotlinx.android.synthetic.main.activity_cocos_home.*

class CocosHomeActivity : BaseActivity() {
    val download1Url = "http://file.jinxianyun.com/default8.zip"
    val download2Url = "http://file.jinxianyun.com/hellococos.zip"

    override fun getLayoutId() = R.layout.activity_cocos_home

    override fun initView() {
        icBack.setOnClickListener {
            finish()
        }

        DownloadUtil.instance.addListener(listener)
        CocosBridgeHelper.getInstance().addMainListener(cocosListenerInMain)

        btnDownload1.setOnClickListener {
            DownloadUtil.instance.download(download1Url)
        }
        btnDownload2.setOnClickListener {
            DownloadUtil.instance.download(download2Url)
        }
    }

    val listener: DownloadListener = object : DownloadListener {
        override fun callBack(taskId: Int, status: Int, progress: Int) {
            Log.i("nell--", "taskId:$taskId, status:$status, progress:$progress, url1taskId:${DownloadUtil.instance.taskMap.get(download1Url)}")

            updateUI(download1Url, btnDownload1, taskId, status, progress)
            updateUI(download2Url, btnDownload2, taskId, status, progress)
        }
    }

    private val cocosListenerInMain: CocosDataListener = CocosDataListener { action, argument, callbackId ->
        CocosBridgeHelper.log("接收InMain", action)
        if (action == "action_appVersion") {
            CocosBridgeHelper.getInstance().main2Cocos(action, packageManager.getPackageInfo(packageName, 0).versionName, callbackId)
        }
    }

    fun updateUI(url: String, btn: AppCompatButton, taskId: Int, status: Int, progress: Int) {
        if (DownloadUtil.instance.taskMap.get(url) == taskId) {
            when (status) {
                DownloadManager.STATUS_RUNNING -> {
                    btn?.text = "下载中${progress}%"
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    btn?.text = "解压中"
                    val zipFileString =
                        "${getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath}/${
                            url.split("/").last()
                        }"
                    val outPathString =
                        getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
                    FileUtil.unzip(
                        zipFileString,
                        outPathString,
                        object : FileUtil.ZipProgress {
                            override fun onProgress(progress: Int) {
                                btn?.text = "解压中$progress"
                            }

                            override fun onDone() {
                                btn?.text = "打开游戏"
                                btn?.setOnClickListener {
                                    val intent = Intent(
                                        this@CocosHomeActivity,
                                        CocosGameActivity::class.java
                                    )
                                    intent.putExtra(
                                        "path",
                                        "${outPathString}/${
                                            url.split("/").last().split(".").first()
                                        }"
                                    )
                                    startActivity(intent)
                                }
                            }
                        }
                    )
                }
                DownloadManager.STATUS_FAILED -> {
                    btn?.text = "下载失败"
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        DownloadUtil.instance.removeListener(listener)
        CocosBridgeHelper.getInstance().removeMainListener(cocosListenerInMain)
    }
}