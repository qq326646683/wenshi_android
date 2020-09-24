package com.jinxian.wenshi.ext

import com.jinxian.wenshi.AppContext

fun getVersionName(): String {
    return AppContext.packageManager.getPackageInfo(AppContext.packageName, 0).versionName
}