package com.jinxian.wenshi.module_discover.opengl.custom_shader

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.jinxian.wenshi.R
import com.jinxian.wenshi.util.ResReadUtil
import com.jinxian.wenshi.util.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL
import javax.microedition.khronos.opengles.GL10

class RectShapeRender : GLSurfaceView.Renderer {
    // 一个FLoat字节数
    private val BYTES_PER_FLOAT = 4

    // 顶点个数
    private val POSITION_COMPONENT_COUNT = 4

    // 顶点位置缓存
    private var vertexBuffer: FloatBuffer? = null

    // 顶点颜色缓存
    private var colorBuffer: FloatBuffer? = null

    // 顶点索引缓存
    private var indicesBuffer: ShortBuffer? = null

    // 渲染程序
    private var mProgram: Int? = null

    // 相机矩阵
    private val mViewMatrix: FloatArray = FloatArray(16)

    // 投影矩阵
    private val mProjectMatrix: FloatArray = FloatArray(16)

    // 最终变化矩阵
    private val mMVPMatrix: FloatArray = FloatArray(16)

    // 返回属性变量位置
    // 变换矩阵
    private var uMatrixLocation: Int? = null

    // 位置
    private var aPositionLocation: Int? = null

    // 颜色
    private var aColorLocation: Int? = null

    // 四个顶点位置参数
    private val rectangleCoords = floatArrayOf(
        -0.5f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f, 0.5f, 0.0f
    )

    // 顶点索引
    private val indices = shortArrayOf(
        0, 1, 2, 0, 2, 3
    )

    // 四个顶点颜色
    private val colors = floatArrayOf(
        0.0f, 0.0f, 1.0f, 1.0f,//top left
        0.0f, 1.0f, 0.0f, 1.0f,// bottom left
        0.0f, 0.0f, 1.0f, 1.0f,// bottom right
        1.0f, 0.0f, 0.0f, 1.0f// top right
    )


    constructor() {
        // 顶点位置相关
        vertexBuffer = ByteBuffer.allocateDirect(rectangleCoords.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexBuffer?.put(rectangleCoords)
        vertexBuffer?.position(0)

        // 顶点颜色相关
        colorBuffer = ByteBuffer.allocateDirect(colors.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()

        colorBuffer?.put(colors)
        colorBuffer?.position(0)

        // 顶点索引相关
        indicesBuffer = ByteBuffer.allocateDirect(indices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()

        indicesBuffer?.put(indices)
        indicesBuffer?.position(0)

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 将背景设置为白色
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        // 编译顶点着色程序
        val vertexShaderStr = ResReadUtil.readResource(R.raw.vertex_matrix_shade)
        val vertexShaderId = ShaderUtil.compileVertexShader(vertexShaderStr)

        // 编译片段着色程序
        val fragmentShaderStr = ResReadUtil.readResource(R.raw.fragment_dotline_shade)
        val fragmentShaderId = ShaderUtil.compileFragmentShader(fragmentShaderStr)

        // 链接程序
        mProgram = ShaderUtil.linkProgram(vertexShaderId, fragmentShaderId)
        // 在OpenGLES环境中使用程序
        mProgram?.let { GLES30.glUseProgram(it) }

        mProgram?.let {
            uMatrixLocation = GLES30.glGetUniformLocation(it, "u_Matrix")
            aPositionLocation = GLES30.glGetAttribLocation(it, "vPosition")
            aColorLocation = GLES30.glGetAttribLocation(it, "aColor")
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        
    }

    override fun onDrawFrame(gl: GL10?) {
    }
}