package com.jinxian.wenshi.module_discover.opengl.activity

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_opengl_dotlinetriangle.*

class OpenGLDotLineTriangleActivity: BaseActivity() {
    override fun getLayoutId() = R.layout.activity_opengl_dotlinetriangle

    override fun initView() {
        glDotLineView.setEGLContextClientVersion(3)
//        glDotLineView.setRenderer(null)
    }
}