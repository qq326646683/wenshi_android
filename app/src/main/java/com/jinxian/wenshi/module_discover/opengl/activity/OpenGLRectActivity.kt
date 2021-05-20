package com.jinxian.wenshi.module_discover.opengl.activity

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_opengl_sample.*

class OpenGLRectActivity: BaseActivity() {
    override fun getLayoutId() = R.layout.activity_opengl_sample

    override fun initView() {
        glSampleView.setEGLContextClientVersion(3)

    }
}