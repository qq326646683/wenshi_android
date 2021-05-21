package com.jinxian.wenshi.module_discover.opengl.custom_shader

import android.opengl.GLES30
import android.opengl.GLSurfaceView
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
        /*
        index：顶点属性的索引.（这里我们的顶点位置和颜色向量在着色器中分别为0和1）layout (location = 0) in vec4 vPosition; layout (location = 1) in vec4 aColor;
        size: 指定每个通用顶点属性的元素个数。必须是1、2、3、4。此外，glvertexattribpointer接受符号常量gl_bgra。初始值为4（也就是涉及颜色的时候必为4）。
        type：属性的元素类型。（上面都是Float所以使用GLES30.GL_FLOAT）；
        normalized：转换的时候是否要经过规范化，true：是；false：直接转化；
        stride：跨距，默认是0。（由于我们将顶点位置和颜色数据分别存放没写在一个数组中，所以使用默认值0）
        ptr： 本地数据缓存（这里我们的是顶点的位置和颜色数据）。
        一层层的去查看我们发现最终实现都是c方法，这也和我们第一篇中说的OpenGL的核心代码都是写的。
         */
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