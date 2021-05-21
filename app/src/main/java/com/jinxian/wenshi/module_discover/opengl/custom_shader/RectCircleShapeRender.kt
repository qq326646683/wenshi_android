package com.jinxian.wenshi.module_discover.opengl.custom_shader

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.jinxian.wenshi.R
import com.jinxian.wenshi.util.ResReadUtil
import com.jinxian.wenshi.util.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos

class RectCircleShapeRender constructor(type: RectOrCircle) : GLSurfaceView.Renderer {
    // 一个FLoat字节数
    private val BYTES_PER_FLOAT = 4

    // 顶点个数
    private val POSITION_COMPONENT_COUNT = 4

    // 矩形还是圆形
    private var mType: RectOrCircle = type

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

    // 圆形顶点位置
    private var circularCoords: FloatArray? = null

    // 圆形顶点的颜色
    private var circularColors: FloatArray? = null

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


    init {
        when (mType) {
            RectOrCircle.Rect -> {
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

            RectOrCircle.Circle -> {
                readyCircleLocationAndColor(1, 60)
                // 顶点位置相关
                circularCoords?.let {
                    vertexBuffer = ByteBuffer.allocateDirect(it.size * BYTES_PER_FLOAT)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
                    vertexBuffer?.put(it)
                    vertexBuffer?.position(0)
                }
                // 顶点颜色相关
                circularColors?.let {
                    colorBuffer = ByteBuffer.allocateDirect(it.size * BYTES_PER_FLOAT)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
                    colorBuffer?.put(it)
                    colorBuffer?.position(0)
                }
            }
        }
    }

    private fun readyCircleLocationAndColor(radius: Int, n: Int) {
        val data = arrayListOf<Float>()
        // 设置圆心坐标
        data.add(0.0f)
        data.add(0.0f)
        data.add(0.0f)
        // 每一份角度
        val angDegSpan: Int = (360f / n).toInt()

        for (i in 0..(360 + angDegSpan) step angDegSpan) {
            data.add((radius * sin(i * PI / 180)).toFloat())
            data.add((radius * cos(i * PI / 180)).toFloat())
            data.add(0.0f)
        }
        circularCoords = FloatArray(data.size) {
            data[it]
        }

        // 各个顶点的颜色
        val tmpColors = arrayListOf<Float>()
        val itemColors = arrayListOf<Float>()
        itemColors.add(1.0f)
        itemColors.add(0.0f)
        itemColors.add(0.0f)
        itemColors.add(1.0f)
        for (i in 0..(data.size / 3)) {
            tmpColors.addAll(itemColors)
        }
        circularColors = FloatArray(tmpColors.size) {
            tmpColors[it]
        }

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
        // 设置绘制窗口
        GLES30.glViewport(0, 0, width, height)

        // 相机和透视投影
        // 计算宽高比
        val ratio: Float = width.toFloat() / height
        // 解释frustumM和setLookAtM:https://blog.csdn.net/jamesshaoya/article/details/54342241
        // 设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
        // 设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 7.0f, 0f, 0f, 0f, 0.0f, 1.0f, 0.0f)
        // 计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 把颜色缓冲区设置为我们预设的颜色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        // 将变换矩阵传到顶点渲染器
        uMatrixLocation?.let { GLES30.glUniformMatrix4fv(it, 1, false, mMVPMatrix, 0) }

        aPositionLocation?.let {
            // 准备坐标数据
            GLES30.glVertexAttribPointer(it, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
            // 启用顶点位置句柄
            GLES30.glEnableVertexAttribArray(it)
        }

        aColorLocation?.let {
            // 准备颜色数据
            GLES30.glVertexAttribPointer(it, 4, GLES30.GL_FLOAT, false, 0, colorBuffer)
            // 启用顶点颜色句柄
            GLES30.glEnableVertexAttribArray(it)
        }

        if (mType == RectOrCircle.Rect) {
            // 绘制矩形
            GLES30.glDrawElements(
                GL10.GL_TRIANGLES,
                indices.size,
                GL10.GL_UNSIGNED_SHORT,
                indicesBuffer
            )
        } else {
            // 绘制圆形
            circularCoords?.let {
                GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, it.size / 3)
            }
        }


        // 禁止顶点数组的句柄
        aPositionLocation?.let { GLES30.glDisableVertexAttribArray(it) }
        aColorLocation?.let { GLES30.glDisableVertexAttribArray(it) }

    }

    enum class RectOrCircle {
        Rect,
        Circle
    }
}