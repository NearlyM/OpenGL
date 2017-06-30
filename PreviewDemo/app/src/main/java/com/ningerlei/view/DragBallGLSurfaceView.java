package com.ningerlei.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.ningerlei.render.BallGLRenderOld;

/**
 * Description :
 * CreateTime : 2017/6/30 10:10
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/30 10:10
 * @ModifyDescription :
 */

public class DragBallGLSurfaceView extends GLSurfaceView {

    private BallGLRenderOld mRenderer;

    private float mDownX = 0.0f;
    private float mDownY = 0.0f;

    public DragBallGLSurfaceView(Context context) {
        super(context);

        mRenderer = new BallGLRenderOld();
        this.setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                float mY = event.getY();
                mRenderer.mLightX += (mX-mDownX)/10;
                mRenderer.mLightY -= (mY-mDownY)/10;
                mDownX = mX;
                mDownY = mY;
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
