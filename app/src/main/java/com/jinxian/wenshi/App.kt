package com.jinxian.wenshi

import android.app.Application
import android.content.ContextWrapper
import com.fmt.launch.starter.TaskDispatcher
import com.jinxian.wenshi.tasks.InitBuglyTask
import com.jinxian.wenshi.tasks.InitKoinTask
import com.jinxian.wenshi.tasks.InitSmartRefreshLayoutTask

lateinit var mApplication: Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        mApplication = this


        //启动器 异步初始化
        TaskDispatcher.init(this)
        TaskDispatcher.createInstance()
            .addTask(InitBuglyTask())
            .addTask(InitKoinTask())
            .addTask(InitSmartRefreshLayoutTask())
            .start()


    }
}

object AppContext: ContextWrapper(mApplication)