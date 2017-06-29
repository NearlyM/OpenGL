package com.ningerlei.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.ningerlei.previewdemo.R;
import com.ningerlei.util.ShaderUtil;
import com.ningerlei.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Description :
 * CreateTime : 2017/6/29 9:47
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/29 9:47
 * @ModifyDescription :
 */

public class PicGLRender implements GLSurfaceView.Renderer {
    private Context context;
    private int aPositionHandle;
    private int programId;
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;

    private int textureId;
    private int uTextureSimplerHandle;
    private int aTextureCoordHandle;
    private FloatBuffer textureBuffer;
    private final float[] vertexData = {
            0f,0f,0f,
            1f,1f,0f,
            -1f,1f,0f,
            -1f,-1f,0f,
            1f,-1f,0f
    };

    private final short[] indexData = {
            0,1,2,
            0,2,3,
            0,3,4,
            0,4,1
    };

    private final float[] textureVertexData = {
            0.5f,0.5f,
            1f,0f,
            0f,0f,
            0f,1f,
            1f,1f
    };

    private final float[] projectionMatrix = new float[16];
    private int uMatrixHandle;

    public PicGLRender(Context context){
        this.context = context;
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indexData.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indexData);
        indexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertexData);
        textureBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertex_shader = ShaderUtil.readRawText(context, R.raw.pic_vertex_shader);
        String fragment_shader = ShaderUtil.readRawText(context, R.raw.pic_fragment_shader);
        programId = ShaderUtil.createProgram(vertex_shader, fragment_shader);
        textureId = TextureHelper.loadTexture(context, R.drawable.james);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uMatrixHandle = GLES20.glGetUniformLocation(programId, "uMatrix");
        uTextureSimplerHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = width > height ? (float) width / height : (float) height / width;
        if (width > height){
            Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f);
        }else {
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(programId);
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, projectionMatrix, 0);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);


        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uTextureSimplerHandle, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
