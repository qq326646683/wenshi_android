package com.jinxian.wenshi.module_discover.opengl.custom_shader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.*
import android.util.Log
import com.jinxian.wenshi.AppContext
import com.jinxian.wenshi.R
import com.jinxian.wenshi.util.ResReadUtil
import com.jinxian.wenshi.util.ShaderUtil
import com.tencent.qgame.animplayer.util.TextureLoadUtil.loadTexture
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Texture2DShapeRender : GLSurfaceView.Renderer {
    private val TAG = "Texture2DShapeRender"

    // 顶点位置缓存
    private var vertexBuffer: FloatBuffer? = null

    // 纹理位置缓存
    private var mTexVertexBuffer: FloatBuffer? = null

    // 顶点索引缓存
    private var mVertexIndexBuffer: ShortBuffer? = null

    private var mProgram: Int? = null

    // 纹理id
    private var textureId: Int? = null

    // 相机矩阵
    private var mViewMatrix: FloatArray = FloatArray(16)

    // 投影矩阵
    private var mProjectMatrix: FloatArray = FloatArray(16)

    // 最终变换矩阵
    private var mMVPMatrix: FloatArray = FloatArray(16)

    // 返回属性变量的位置
    // 变换矩阵
    private var uMatrixLocation: Int? = null

    // 顶点
    private var aPositionLocation: Int? = null

    // 纹理
    private var aTextureLocation: Int? = null

    /**
     * 顶点坐标
     */
    private val POSITION_VERTEX = floatArrayOf(
        0f, 0f, 0f, // 顶点坐标V0
        1f, 1f, 0f, // 顶点坐标V1
        -1f, 1f, 0f, // 顶点坐标V2
        -1f, -1f, 0f, // 顶点坐标V3
        1f, -1f, 0f // 顶点坐标V4
    )

    /**
     * 纹理坐标
     */
    private val TEX_VERTEX = floatArrayOf(
        0.5f, 0.5f, // 顶点坐标V0
        1f, 0f, // 顶点坐标V1
        0f, 0f, // 顶点坐标V2
        0f, 1f, // 顶点坐标V3
        1f, 1f // 顶点坐标V4
    )

    /**
     * 绘制顺序索引
     */
    private val VERTEX_INDEX = shortArrayOf(
        0, 1, 2,
        0, 2, 3,
        0, 3, 4,
        0, 4, 1
    )

    // 图片生成的位图
    private var mBitmap: Bitmap? = null

    constructor() {
        vertexBuffer = ByteBuffer.allocateDirect(POSITION_VERTEX.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexBuffer?.put(POSITION_VERTEX)
        vertexBuffer?.position(0)

        mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        mTexVertexBuffer?.put(TEX_VERTEX)
        mTexVertexBuffer?.position(0)

        mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
        mVertexIndexBuffer?.put(VERTEX_INDEX)
        mVertexIndexBuffer?.position(0)
    }


    override fun onSurfaceCreated(pl: GL10?, config: EGLConfig?) {
        // 将背景设置为白色
        GLES30.glClearColor(1f, 1f, 1f, 1f)

        // 编译
        val vertexShaderId =
            ShaderUtil.compileVertexShader(ResReadUtil.readResource(R.raw.vertex_texture2d_shade))
        val fragmentShaderId =
            ShaderUtil.compileFragmentShader(ResReadUtil.readResource(R.raw.fragment_texture2d_shade))
        // 链接程序片段
        mProgram = ShaderUtil.linkProgram(vertexShaderId, fragmentShaderId)
        mProgram?.let {
            // 在OpenGL ES环境中使用程序
            GLES30.glUseProgram(it)

            // 获取属性位置
            uMatrixLocation = GLES30.glGetUniformLocation(it, "u_Matrix")
            aPositionLocation = GLES30.glGetAttribLocation(it, "vPosition")
            aTextureLocation = GLES30.glGetAttribLocation(it, "aTextureCoord")
        }

        // 加载纹理
        textureId = loadTexture(R.mipmap.welcome_bg)
    }

    private fun loadTexture(resourceId: Int): Int {
        val textureIds = IntArray(1)
        // 创建纹理对象
        GLES30.glGenTextures(1, textureIds, 0)
        if (textureIds[0] == 0) {
            Log.e(TAG, "Could not generate a new OpenGL textureId object.")
            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false
        mBitmap = BitmapFactory.decodeResource(AppContext.resources, resourceId, options)
        if (mBitmap == null) {
            Log.e(TAG, "Resource ID $resourceId could not be decoded.")
            return 0
        }
        // 绑定纹理到OpenGL
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0])

        // 设置默认的纹理过滤参数
        GLES30.glTexParameteri(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_LINEAR_MIPMAP_LINEAR
        )
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)

        // 加载bitmap到纹理中
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, mBitmap, 0);

        // 生成MIP贴图
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)

        // 数据如果已经加载进Opengl，回收该bitmap
        mBitmap?.recycle()

        // 取消绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)

        return textureIds[0]
    }

    override fun onSurfaceChanged(pl: GL10?, width: Int, height: Int) {
        // 设置绘制窗口
        GLES30.glViewport(0, 0, width, height)
        mBitmap?.let {
            val w = it.width
            val h = it.height
            val sWH = w.toFloat() / h
            val sWidthHeight = width.toFloat() / height
            if (width > height) {
                if (sWH > sWidthHeight) {
                    Matrix.orthoM(
                        mProjectMatrix, 0, -sWidthHeight * sWH,
                        sWidthHeight * sWH, -1f, 1f, 3f, 7f
                    )
                } else {
                    Matrix.orthoM(
                        mProjectMatrix, 0, (-sWidthHeight / sWH).toFloat(),
                        sWidthHeight / sWH, -1f, 1f, 3f, 7f
                    )
                }
            } else {
                if (sWH > sWidthHeight) {
                    Matrix.orthoM(
                        mProjectMatrix, 0, -1f, 1f,
                        -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH, 3f, 7f
                    )
                } else {
                    Matrix.orthoM(
                        mProjectMatrix, 0, -1f, 1f,
                        -sWH / sWidthHeight, sWH / sWidthHeight, 3f, 7f
                    )
                }
            }

        }

        // 设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        // 计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(pl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        // 将变换矩阵传入顶点渲染
        uMatrixLocation?.let { GLES30.glUniformMatrix4fv(it, 1, false, mMVPMatrix, 0) }
        // 启用顶点坐标属性
        aPositionLocation?.let {
            GLES30.glEnableVertexAttribArray(it)
            GLES30.glVertexAttribPointer(it, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        }
        // 启用纹理坐标属性
        aTextureLocation?.let {
            GLES30.glEnableVertexAttribArray(it)
            GLES30.glVertexAttribPointer(it, 2, GLES30.GL_FLOAT, false, 0, mTexVertexBuffer)
        }
        // 激活纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        // 绑定纹理
        textureId?.let { GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, it) }
        // 绘制
        GLES30.glDrawElements(
            GLES20.GL_TRIANGLES,
            VERTEX_INDEX.size,
            GLES20.GL_UNSIGNED_SHORT,
            mVertexIndexBuffer
        )

        // 禁止顶点数组的句柄
        aPositionLocation?.let { GLES30.glDisableVertexAttribArray(it) }
        aTextureLocation?.let { GLES30.glDisableVertexAttribArray(it) }
    }
}