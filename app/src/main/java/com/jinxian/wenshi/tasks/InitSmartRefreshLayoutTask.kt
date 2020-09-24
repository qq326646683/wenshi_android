package com.jinxian.wenshi.tasks

import androidx.core.content.ContextCompat
import com.fmt.launch.starter.task.Task
import com.jinxian.wenshi.R
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout

class InitSmartRefreshLayoutTask: Task() {
    override fun run() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setEnableHeaderTranslationContent(false)
            MaterialHeader(context).setColorSchemeColors(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
        }
    }
}