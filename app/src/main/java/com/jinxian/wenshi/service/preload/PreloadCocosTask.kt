package com.jinxian.wenshi.service.preload

import android.os.Environment
import com.jinxian.wenshi.AppContext
import com.jinxian.wenshi.module_discover.cocos.CocosHomeActivity
import com.jinxian.wenshi.module_discover.cocos.CocosHomeActivity.Companion.download1Url
import com.jinxian.wenshi.module_discover.cocos.CocosHomeActivity.Companion.download2Url
import com.jinxian.wenshi.util.FileUtil

class PreloadCocosTask(private val zipUrl: String): PreloadTask() {
    override fun getUrl(): String = zipUrl

    override fun getCacheDir(): String = AppContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath!!

    override fun onLoadSuccess(path: String?) {
        super.onLoadSuccess(path)
        val zipFileString = path
        val outPathString =
            AppContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
        FileUtil.unzip(
            zipFileString,
            outPathString,
            object : FileUtil.ZipProgress {
                override fun onProgress(progress: Int) {
                }

                override fun onDone() {
                    if (url == download1Url) {
                        CocosHomeActivity.isUrl1Succ = true
                    }
                    if (url == download2Url) {
                        CocosHomeActivity.isUrl2Succ = true
                    }
                }
            }
        )
    }
}