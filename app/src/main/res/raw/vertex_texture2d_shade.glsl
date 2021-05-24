#version 300 es
layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec2 aTextureCoord;
uniform mat4 u_Matrix;
// 输出纹理坐标
out vec2 vTexCoord;

void main() {
    gl_Position = u_Matrix * vPosition;
    gl_PointSize = 10.0;
    vTexCoord = aTextureCoord;
}
