package com.ningerlei.previewdemo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ningerlei.render.RectGLRender;
import com.ningerlei.render.TriangleGLRender;

/**
 * Description :
 * CreateTime : 2017/6/28 14:47
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/28 14:47
 * @ModifyDescription :
 */

public class GLSurfaceViewActivity extends PreviewActivity {

    GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glsurfaceview);
        findViewById();
        initGLSurfaceView();
    }

    private void findViewById() {
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
    }

    private void initGLSurfaceView() {
        glSurfaceView.setEGLContextClientVersion(2);
//        glSurfaceView.setRenderer(new TriangleGLRender(this));
        glSurfaceView.setRenderer(new RectGLRender(this));
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }
}
