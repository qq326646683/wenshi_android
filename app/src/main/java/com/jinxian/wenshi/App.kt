package com.jinxian.wenshi

import android.app.Application
import com.fmt.launch.starter.TaskDispatcher

lateinit var mApplication: Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        mApplication = this


        //启动器 异步初始化
        TaskDispatcher.init(this)
        TaskDispatcher.createInstance()
//            .addTask()
            .start()
    }
}