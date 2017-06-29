package com.ningerlei.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.ningerlei.previewdemo.R;
import com.ningerlei.shape.Sphere;
import com.ningerlei.shape.SphereNoTexture;
import com.ningerlei.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Description :
 * CreateTime : 2017/6/29 18:19
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/29 18:19
 * @ModifyDescription :
 */

public class BallGLRender implements GLSurfaceView.Renderer{

    private Context context;
    private int aPositionHandle;
    private int programId;

    private int uMatrixHandle;

    private Sphere sphere;

    private float[] modelMatrix = new float[16];
    private float[] projectionMatrix=new float[16];
    private float[] viewMatrix = new float[16];
    private float[] modelViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int screenWidth;
    private int screenHeight;

    public BallGLRender(Context context){
        this.context = context;
        sphere = new Sphere(18f, 100, 200);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertex_shader = ShaderUtil.readRawText(context, R.raw.triangle_vertex_shader);
        String fragment_shader = ShaderUtil.readRawText(context, R.raw.triangle_fragment_shader);
        programId = ShaderUtil.createProgram(vertex_shader, fragment_shader);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uMatrixHandle = GLES20.glGetUniformLocation(programId, "uMatrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenWidth=width; screenHeight=height;
        float ratio=(float)width/height;
        Matrix.perspectiveM(projectionMatrix, 0, 90, ratio, 1f, 500f);

        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f,-1.0f,
                0.0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(programId);
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, projectionMatrix, 0);
        GLES20.glEnableVertexAttribArray(aPositionHandle);

        Matrix.setIdentityM(modelMatrix,0);
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(uMatrixHandle,1,false,mMVPMatrix,0);
    }
}
