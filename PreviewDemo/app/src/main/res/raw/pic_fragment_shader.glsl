precision mediump float;
varying vec2 vTexCoord;
uniform sampler2D sTexture; //sampler2D表示一个纹理数据数组
void main() {
    gl_FragColor = texture2D(sTexture, vTexCoord);  //texture2D()来处理被插值的纹理坐标sTexture和纹理数据vTexCoord，得到的显色就是要显示的颜色
}
