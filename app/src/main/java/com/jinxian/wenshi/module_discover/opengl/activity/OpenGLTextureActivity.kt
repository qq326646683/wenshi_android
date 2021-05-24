package com.jinxian.wenshi.module_discover.opengl.activity

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import com.jinxian.wenshi.module_discover.opengl.custom_shader.Texture2DShapeRender
import kotlinx.android.synthetic.main.activity_opengl_sample.*

class OpenGLTextureActivity: BaseActivity() {
    override fun getLayoutId() = R.layout.activity_opengl_sample

    override fun initView() {
        glSampleView.setEGLContextClientVersion(3)
        glSampleView.setRenderer(Texture2DShapeRender())
    }
}