package com.ningerlei.shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.ningerlei.util.ShaderUtil.checkGlError;

/**
 * Description :
 * CreateTime : 2017/6/30 16:33
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/30 16:33
 * @ModifyDescription :
 */

public class SphereNoTexture implements Shape{

    FloatBuffer verticalsBuffer;
    float[] verticals = new float[20 * 40 * 6 * 3];
    ShortBuffer verticalsIndexBuffer;
    //每个三角形的定点编号，多边形有两个三角形组成
    short[] verticalsIndex = new short[]{
            0, 1, 2,
            0, 2, 3
    };

    public SphereNoTexture(){
        float x = 0;
        float y = 0;
        float z = -1;
        float r = 1;
        int index=0;
        double d = 9 * Math.PI / 180;
        for (int i = 0; i < 180; i += 9) {
            double d1 = i * Math.PI / 180;
            for (int j = 0; j < 360; j += 9) {
                double d2 = j * Math.PI / 180;
                verticals[index++] = (float) (x+r*Math.sin(d1+d)*Math.cos(d2+d));
                verticals[index++] = (float) (y+r*Math.cos(d1+d));
                verticals[index++] = (float) (z+r*Math.sin(d1+d)*Math.sin(d2+d));

                verticals[index++] = (float) (x+r*Math.sin(d1)*Math.cos(d2));
                verticals[index++] = (float) (y+r*Math.cos(d1));
                verticals[index++] = (float) (z+r*Math.sin(d1)*Math.sin(d2));

                verticals[index++] = (float) (x+r*Math.sin(d1)*Math.cos(d2+d));
                verticals[index++] = (float) (y+r*Math.cos(d1));
                verticals[index++] = (float) (z+r*Math.sin(d1)*Math.sin(d2+d));

                verticals[index++] = (float) (x+r*Math.sin(d1+d)*Math.cos(d2+d));
                verticals[index++] = (float) (y+r*Math.cos(d1+d));
                verticals[index++] = (float) (z+r*Math.sin(d1+d)*Math.sin(d2+d));

                verticals[index++] = (float) (x+r*Math.sin(d1+d)*Math.cos(d2));
                verticals[index++] = (float) (y+r*Math.cos(d1+d));
                verticals[index++] = (float) (z+r*Math.sin(d1+d)*Math.sin(d2));

                verticals[index++] = (float) (x+r*Math.sin(d1)*Math.cos(d2));
                verticals[index++] = (float) (y+r*Math.cos(d1));
                verticals[index++] = (float) (z+r*Math.sin(d1)*Math.sin(d2));

            }
        }

        verticalsBuffer = ByteBuffer.allocateDirect(verticals.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(verticals);
        verticalsBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (对应顺序的坐标数 * 2)short是2字节
                verticalsIndex.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        verticalsIndexBuffer = dlb.asShortBuffer();
        verticalsIndexBuffer.put(verticalsIndex);
        verticalsIndexBuffer.position(0);
    }

    @Override
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,20 * 40 * 6 );
    }

    @Override
    public void uploadVerticesBuffer(int positionHandle) {
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false,
                12, verticalsBuffer);
    }

    @Override
    public void uploadTexCoordinateBuffer(int textureCoordinateHandle) {

    }
}
