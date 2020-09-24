package com.jinxian.wenshi.tasks

import com.fmt.launch.starter.task.MainTask
import com.fmt.launch.starter.task.Task
import com.jinxian.wenshi.di.appModule
import org.koin.core.context.startKoin

class InitKoinTask : Task() {
    override fun run() {
        startKoin {
            modules(appModule)
        }
    }
}