package com.jinxian.wenshi.module_discover.opengl.activity

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import com.jinxian.wenshi.module_discover.opengl.custom_shader.RectCircleShapeRender
import kotlinx.android.synthetic.main.activity_opengl_rectcircle.*

class OpenGLRectCircleActivity: BaseActivity() {
    override fun getLayoutId() = R.layout.activity_opengl_rectcircle

    override fun initView() {
        glRectView.setEGLContextClientVersion(3)
        glRectView.setRenderer(RectCircleShapeRender(RectCircleShapeRender.RectOrCircle.Rect))

        glCircleView.setEGLContextClientVersion(3)
        glCircleView.setRenderer(RectCircleShapeRender(RectCircleShapeRender.RectOrCircle.Circle))
    }
}