package com.jinxian.wenshi.util

import android.opengl.GLES30
import android.util.Log
import kotlin.math.log

object ShaderUtil {
    val TAG = "ShaderUtil"

    /**
     * 编译顶点着色器
     */
    fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GLES30.GL_VERTEX_SHADER, shaderCode)
    }

    /**
     * 编译片段着色器
     */
    fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GLES30.GL_FRAGMENT_SHADER, shaderCode)
    }

    /**
     * 链接程序
     */
    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        val programId = GLES30.glCreateProgram()
        if (programId != 0) {
            // 将顶点着色器加入程序
            GLES30.glAttachShader(programId, vertexShaderId)
            // 将片段着色器加入程序
            GLES30.glAttachShader(programId, fragmentShaderId)
            // 链接着色器程序
            GLES30.glLinkProgram(programId)
            // 检查状态
            val linkStatus = intArrayOf(0)
            GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                val logInfo = GLES30.glGetProgramInfoLog(programId)
                Log.e(TAG, logInfo)
                GLES30.glDeleteProgram(programId)
                return 0
            }
            return programId
        } else {
            return 0
        }
    }

    /**
     * 编译
     * @param type  顶点着色:GLES30.GL_VERTEX_SHADER
     *              片段着色:GLES30.GL_FRAGMENT_SHADER
     * @param shaderCode
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        // 创建着色器
        val shaderId = GLES30.glCreateShader(type)
        if (shaderId != 0) {
            GLES30.glShaderSource(shaderId, shaderCode)
            GLES30.glCompileShader(shaderId)
            // 检查状态
            val compileStatus = intArrayOf(0)
            GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] == 0) {
                val logInfo = GLES30.glGetShaderInfoLog(shaderId)
                Log.e(TAG, logInfo)
                // 创建失败
                GLES30.glDeleteShader(shaderId)
                return 0
            }
            return shaderId
        } else {
            return 0
        }
    }
}