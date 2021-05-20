package com.jinxian.wenshi.module_discover.opengl.custom_shader

import android.graphics.Shader
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import com.jinxian.wenshi.R
import com.jinxian.wenshi.util.ResReadUtil
import com.jinxian.wenshi.util.ShaderUtil
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
        colorBuffer?.position(0)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 背景设置为白色
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        // 编译顶点着色程序
        val vertexShaderStr = ResReadUtil.readResource(R.raw.vertex_dotline_shade)
        val vertexShaderId = ShaderUtil.compileVertexShader(vertexShaderStr)

        // 编译片段着色程序
        val fragmentShaderStr = ResReadUtil.readResource(R.raw.fragment_dotline_shade)
        val fragmentShaderId = ShaderUtil.compileFragmentShader(fragmentShaderStr)

        // 连接程序
        mProgram = ShaderUtil.linkProgram(vertexShaderId, fragmentShaderId)

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
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        // 启用顶点位置句柄
        GLES30.glEnableVertexAttribArray(0)

        // 准备颜色数据
        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 0, colorBuffer)
        // 启用顶点颜色句柄
        GLES30.glEnableVertexAttribArray(1)

        // 绘制三个点
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, POSITION_COMPONENT_COUNT)

        // 绘制三条线
        GLES30.glLineWidth(3f)
        GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 0, POSITION_COMPONENT_COUNT)

        // 绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT)

        // 禁止顶点数据的句柄
        GLES30.glDisableVertexAttribArray(0)
        GLES30.glDisableVertexAttribArray(1)
    }
}