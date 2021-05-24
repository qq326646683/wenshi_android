package com.jinxian.wenshi.module_discover.opengl.activity

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import com.jinxian.wenshi.ext.startActivity
import kotlinx.android.synthetic.main.activity_opengl_home.*

class OpenGLHomeActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_opengl_home

    override fun initView() {
        llOpenGLSample.setOnClickListener {
            startActivity<OpenGLSampleActivity>(false)
        }
        icBack.setOnClickListener {
            finish()
        }

        llOpenGLDotLine.setOnClickListener {
            startActivity<OpenGLDotLineTriangleActivity>(false)
        }

        llOpenRect.setOnClickListener {
            startActivity<OpenGLRectCircleActivity>(false)
        }

        llOpenTexture.setOnClickListener {
            startActivity<OpenGLTextureActivity>(false)

        }
    }
}