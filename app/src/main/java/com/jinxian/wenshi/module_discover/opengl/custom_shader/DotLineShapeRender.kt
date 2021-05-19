package com.jinxian.wenshi.module_discover.opengl.custom_shader

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class DotLineShapeRender : GLSurfaceView.Renderer {
    // 一个float占四个字节
    private val BYTES_PER_FLOAT = 4

    // 顶点数量
    private val POSITION_COMPONENT_COUNT = 3

    // 顶点位置缓存
    private var vertexBuffer: FloatBuffer? = null

    // 顶点颜色缓存
    private var colorBuffer: FloatBuffer? = null

    // 渲染程序
    private var mProgram: Int? = null

    // 三个顶点位置参数
    val triangleCoords = floatArrayOf(
        0.5f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )
    // 三个顶点颜色参数
    val colors = floatArrayOf(
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f
    )

    constructor() {
        // 顶点位置初始化
        vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexBuffer?.put(triangleCoords)
        vertexBuffer?.position(0)

        // 顶点颜色初始化
        colorBuffer = ByteBuffer.allocateDirect(colors.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        colorBuffer?.put(colors)
        vertexBuffer?.position(0)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 背景设置为白色
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        // 编译顶点着色程序


        // 编译片段着色程序


        // 连接程序

        // 在OpenGLES环境使用程序
        mProgram?.let { GLES30.glUseProgram(it) }

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 设置绘制窗口
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 把颜色缓冲区设置为预设的颜色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        // 准备坐标数据
        // 启用顶点位置句柄

        // 准备颜色数据
        // 启用顶点颜色句柄

        // 绘制三个点
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, POSITION_COMPONENT_COUNT)

        // 禁止顶点数据的句柄
        GLES30.glDisableVertexAttribArray(0)
        GLES30.glDisableVertexAttribArray(1)
    }
}