package com.ningerlei.render;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.ningerlei.previewdemo.R;
import com.ningerlei.shape.Shape;
import com.ningerlei.shape.Sphere;
import com.ningerlei.shape.SphereNoTexture;
import com.ningerlei.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Description :
 * CreateTime : 2017/6/30 10:43
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/30 10:43
 * @ModifyDescription :
 */

public class BallGLRender implements GLSurfaceView.Renderer {

    //多边形定点
       /* float[] verticals = new float[]{
                -1, -1, 0.0f, // top
                -1f, 1, 0.0f, // bottom left
                1f, 1, 0.0f,  // bottom right
               // 1,-0.5f,0.0f,
        };*/
    private float[] projectMatrix = new float[16];
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMatricHandle;

    private Context context;

    private Shape shape;

    public BallGLRender(Context context) {

        this.context = context;
        shape = new SphereNoTexture();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShader = ShaderUtil.readRawText(context, R.raw.ball_vertex_shader);
        String fragmentShader = ShaderUtil.readRawText(context, R.raw.ball_fragment_shader);

        mProgram = ShaderUtil.createProgram(vertexShader, fragmentShader);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "u_Color");
        mMatricHandle = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float aspectRatio = width > height ? width * 1f / height : height * 1f / width;
        if (width > height) {
            Matrix.orthoM(projectMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(projectMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
//                12, verticalsBuffer);
        shape.uploadVerticesBuffer(mPositionHandle);
        GLES20.glUniform4fv(mColorHandle, 1, new float[]{0, 1, 1, 5}, 0);
        GLES20.glUniformMatrix4fv(mMatricHandle, 1, false, projectMatrix, 0);
        shape.draw();
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, verticalsIndex.length, GLES20.GL_UNSIGNED_SHORT, verticalsIndexBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
