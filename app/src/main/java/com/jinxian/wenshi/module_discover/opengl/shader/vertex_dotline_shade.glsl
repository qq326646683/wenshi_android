// 着色器的版本3.0，OpenGL ES 2.0版本可以不写。
#version 300 es
// 输入一个名为vPosition的4分量向量，layout (location = 0)表示这个变量的位置是顶点属性0。
layout (location = 0) in vec4 vPosition;
// 输入一个名为aColor的4分量向量，layout (location = 1)表示这个变量的位置是顶点属性1。
layout (location = 1) in vec4 aColor;
// 输出一个名为vColor的4分量向量，后面输入到片段着色器中。
out vec4 vColor;
void main() {
    //gl_Position，gl_PointSize为Shader内置变量，分别为顶点位置，点的直径。

    // gl_Position赋值为vPosition
    gl_Position = vPosition;
    // gl_PointSize 绘制点的直径10
    gl_PointSize = 10.0;
    // 将输入数据aColor拷贝到vColor的变量中。
    vColor = aColor;
}
