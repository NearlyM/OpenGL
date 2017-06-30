package com.ningerlei.shape;

import android.opengl.GLES20;

import com.ningerlei.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Description :
 * CreateTime : 2017/6/29 17:40
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/29 17:40
 * @ModifyDescription :
 */

public class Plain implements Shape{

    private FloatBuffer vertexBuffer;

    private final float[] vertexData = {
            1f,-1f,0f,
            -1f,-1f,0f,
            1f,1f,0f,
            -1f,1f,0f
    };
    private FloatBuffer textureVertexBuffer;
    private final float[] textureVertexData = {
            1f,0f,
            0f,0f,
            1f,1f,
            0f,1f
    };

    public Plain(){
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertexData);
        textureVertexBuffer.position(0);
    }

    @Override
    public void uploadVerticesBuffer(int positionHandle){
        if (vertexBuffer == null) return;
        vertexBuffer.position(0);

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        ShaderUtil.checkGlError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        ShaderUtil.checkGlError("glEnableVertexAttribArray maPositionHandle");
    }

    @Override
    public void uploadTexCoordinateBuffer(int coordinateHandle){
        if (textureVertexBuffer == null) return;
        textureVertexBuffer.position(0);

        GLES20.glVertexAttribPointer(coordinateHandle, 2, GLES20.GL_FLOAT, false, 0, textureVertexBuffer);
        ShaderUtil.checkGlError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray(coordinateHandle);
        ShaderUtil.checkGlError("glEnableVertexAttribArray maTextureHandle");
    }

    @Override
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public FloatBuffer getTextureVertexBuffer() {
        return textureVertexBuffer;
    }
}
