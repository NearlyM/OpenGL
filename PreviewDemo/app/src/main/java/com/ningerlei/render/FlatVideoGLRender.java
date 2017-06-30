package com.ningerlei.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Surface;

import com.ningerlei.previewdemo.R;
import com.ningerlei.shape.Plain;
import com.ningerlei.shape.Shape;
import com.ningerlei.util.ShaderUtil;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Description :
 * CreateTime : 2017/6/29 10:45
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/29 10:45
 * @ModifyDescription :
 */

public class FlatVideoGLRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener, MediaPlayer.OnVideoSizeChangedListener{

    private Context context;
    private int programId;
    private int aPositionHandle;
//    private FloatBuffer vertexBuffer;
////    private final float[] vertexData = {
////            1f,1f,0f,
////            -1f,1f,0f,
////            1f,-1f,0f,
////            -1f,-1f,0f
////    };
//
//    private final float[] vertexData = {
//            1f,-1f,0f,
//            -1f,-1f,0f,
//            1f,1f,0f,
//            -1f,1f,0f
//    };

    private final float[] projectionMatrix = new float[16];
    private int uMatrixHandle;

//    private FloatBuffer textureVertexBuffer;
//    private final float[] textureVertexData = {
//            1f,0f,
//            0f,0f,
//            1f,1f,
//            0f,1f
//    };
    private int uTextureSamplerHandle;
    private int aTextureCoordHandle;
    private int textureId;

    private MediaPlayer mediaPlayer;
    private SurfaceTexture surfaceTexture;
    private float[] mSTMatrix = new float[16];
    private int uSTMatrixHandle;

    private boolean updateSurface;
    private boolean playerPrepared;
    private int screenWidth, screenHeight;

    Shape plain;

    public FlatVideoGLRender(Context context, String videoPath){
        this.context = context;
        synchronized (this){
            updateSurface = false;
        }

        plain = new Plain();
//        plain = new SphereNoTextureOld(50f, 100, 200);

//        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer()
//                .put(vertexData);
//        vertexBuffer.position(0);

//        textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer()
//                .put(textureVertexData);
//        textureVertexBuffer.position(0);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnVideoSizeChangedListener(this);
        try {
//            AssetFileDescriptor descriptor = context.getAssets().openFd("test.mp4");
//            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mediaPlayer.setDataSource(context, Uri.parse(videoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShader = ShaderUtil.readRawText(context, R.raw.flatvideo_vertex_shader);
        String fragmentShader = ShaderUtil.readRawText(context, R.raw.flatvideo_fragment_shader);

        programId = ShaderUtil.createProgram(vertexShader, fragmentShader);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uMatrixHandle = GLES20.glGetUniformLocation(programId, "uMatrix");
        uSTMatrixHandle = GLES20.glGetUniformLocation(programId, "uSTMatrix");
        uTextureSamplerHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        textureId = textures[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        ShaderUtil.checkGlError("glBindTexture mTextureID");

        /*GL_TEXTURE_EXTERNAL_OES
        之前提到视频解码的输出格式是YUV的（YUV420sp，应该是），那么这个扩展纹理的作用就是实现YUV格式到RGB的自动转化，
        我们就不需要再为此写YUV转RGB的代码了（如果你搜索相机Preview相关资料，会发现大量的YUV转RGB的实现）*/
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        surfaceTexture = new SurfaceTexture(textureId);
        surfaceTexture.setOnFrameAvailableListener(this);

        Surface surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);
        surface.release();;

        if (!playerPrepared){
            try {
                mediaPlayer.prepare();
                playerPrepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            playerPrepared = true;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        synchronized (this){
            if (updateSurface){
                surfaceTexture.updateTexImage();
                surfaceTexture.getTransformMatrix(mSTMatrix);
                updateSurface = false;
            }
        }

        GLES20.glUseProgram(programId);
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, projectionMatrix, 0);
        GLES20.glUniformMatrix4fv(uSTMatrixHandle, 1, false, mSTMatrix, 0);

//        vertexBuffer.position(0);
//        GLES20.glEnableVertexAttribArray(aPositionHandle);
//        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT,false, 12, vertexBuffer);

        plain.uploadVerticesBuffer(aPositionHandle);
        plain.uploadTexCoordinateBuffer(aTextureCoordHandle);

//        ((SphereNoTextureOld) plain).uploadVerticesBuffer(aPositionHandle);

//        textureVertexBuffer.position(0);
//        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
//        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureVertexBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);

        GLES20.glUniform1i(uTextureSamplerHandle, 0);
        GLES20.glViewport(0, 0, screenWidth, screenHeight);

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        plain.draw();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //监听是否有新的一帧数据到来
        updateSurface = true;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        updateProjection(width,height);
    }

    private void updateProjection(int width, int height) {
        float screenRadio = (float) screenWidth / screenHeight;
        float videoRadio = (float) width / height;
        if (videoRadio > screenRadio){
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -videoRadio / screenRadio, videoRadio / screenRadio, -1f, 1f);
        }else {
            Matrix.orthoM(projectionMatrix, 0, -screenRadio / videoRadio, screenRadio / videoRadio, -1f, 1f, -1f, 1f);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
