package com.jinxian.wenshi.tasks

import com.fmt.launch.starter.task.MainTask
import com.jinxian.wenshi.config.Configs
import com.tencent.bugly.crashreport.CrashReport

class InitBuglyTask : MainTask() {
    override fun run() {
        CrashReport.initCrashReport(mContext, Configs.BUGLY_APP_ID, false)
    }
}