package com.jinxian.wenshi.util

import android.opengl.GLES30

object ShaderUtil {

    /**
     * 编译
     * @param type  顶点着色:GLES30.GL_VERTEX_SHADER
     *              片段着色:GLES30.GL_FRAGMENT_SHADER
     * @param shaderCode
     */
    fun compileShader(type: Int, shaderCode: String) {
        // 创建着色器
        val shaderId = GLES30.glCreateShader(type)
    }
}