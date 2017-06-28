precision mediump float; //precision mediump 用来指定运算的精度，以提高效率

void main(){
    gl_FragColor = vec4(0, 0.5, 0.5, 1); //gl_FragColor 也是一个内建的变量，表示颜色，以rgba的方式排布，范围是[0,1]的浮点数
}