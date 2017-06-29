attribute vec4 aPosition;   //attribute : 用来在Java和vertex shader之间传递变量的
uniform mat4 uMatrix;       //uniform是给vertex shader和fragment shader传递常量的
attribute vec2 aTexCoord;   //二维向量，表示纹理坐标
varying vec2 vTexCoord;     //varying这个变量是用来在vertex shader和fragment shader之间传递数据的，所以名称要相同。
void main(){
    vTexCoord = aTexCoord;
    gl_Position = uMatrix * aPosition; //gl_Position 是OpenGL ES内建变量，表示顶点坐标（xyzw，w是用来进行投影变换的归一化变量）
}