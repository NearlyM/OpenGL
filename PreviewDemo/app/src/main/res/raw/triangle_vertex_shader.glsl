attribute vec4 aPosition;   //attribute : 用来在Java和vertex shader之间传递变量的
uniform mat4 uMatrix;       //uniform是给vertex shader和fragment shader传递常量的
void main(){
    gl_Position = uMatrix * aPosition; //gl_Position 是OpenGL ES内建变量，表示顶点坐标（xyzw，w是用来进行投影变换的归一化变量）
}