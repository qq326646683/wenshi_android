#version 300 es
layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec4 aColor;
uniform mat4 u_Matrix;
out vec4 vColor;
void main() {
    gl_Position = u_Matrix * vPosition;
    gl_PointSize = 10.0;
    vColor = aColor;
}
