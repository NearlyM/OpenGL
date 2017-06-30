package com.ningerlei.shape;

import android.opengl.GLES20;

import com.ningerlei.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Description :
 * CreateTime : 2017/6/29 17:55
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/29 17:55
 * @ModifyDescription :
 */

public class SphereNoTextureOld implements Shape{

    public void draw(GL10 gl) {
        float angleA, angleB;
        float cos, sin;
        float r1, r2;
        float h1, h2;
        float step = 2.0f;
        float[][] v = new float[32][3];
        ByteBuffer vbb;
        FloatBuffer vBuf;

        vbb = ByteBuffer.allocateDirect(v.length * v[0].length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vBuf = vbb.asFloatBuffer();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        for (angleA = -90.0f; angleA < 90.0f; angleA += step) {
            int n = 0;

            r1 = (float)Math.cos(angleA * Math.PI / 180.0);
            r2 = (float)Math.cos((angleA + step) * Math.PI / 180.0);
            h1 = (float)Math.sin(angleA * Math.PI / 180.0);
            h2 = (float)Math.sin((angleA + step) * Math.PI / 180.0);

            // 固定纬度, 360 度旋转遍历一条纬线
            for (angleB = 0.0f; angleB <= 360.0f; angleB += step) {

                cos = (float)Math.cos(angleB * Math.PI / 180.0);
                sin = -(float)Math.sin(angleB * Math.PI / 180.0);

                v[n][0] = (r2 * cos);
                v[n][1] = (h2);
                v[n][2] = (r2 * sin);
                v[n + 1][0] = (r1 * cos);
                v[n + 1][1] = (h1);
                v[n + 1][2] = (r1 * sin);

                vBuf.put(v[n]);
                vBuf.put(v[n + 1]);

                n += 2;

                if(n>31){
                    vBuf.position(0);

                    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);
                    gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);
                    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);

                    n = 0;
                    angleB -= step;
                }

            }
            vBuf.position(0);

            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }

    @Override
    public void draw() {

    }

    @Override
    public void uploadVerticesBuffer(int positionHandle) {

    }

    @Override
    public void uploadTexCoordinateBuffer(int textureCoordinateHandle) {

    }
}
