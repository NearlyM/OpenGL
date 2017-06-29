package com.ningerlei.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Description :
 * CreateTime : 2017/6/29 9:11
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/29 9:11
 * @ModifyDescription :
 */

public class TextureHelper {
    private static final String TAG = TextureHelper.class.getSimpleName();

    public static int loadTexture(Context context, int resId){

        final int[] textureObjId = new int[1];
        GLES20.glGenTextures(1, textureObjId, 0);   //生成一个纹理
        if (textureObjId[0] == 0){
            Log.d(TAG, "生成纹理失败");
            return 0;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap == null){
            Log.d(TAG, "位图加载失败");
            GLES20.glDeleteTextures(1, textureObjId, 0);
            return 0;
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjId[0]);    //将刚生成的纹理OpenGL的2D纹理绑定，告诉OpenGL这是一个2D纹理

        //设置纹理过滤的模式： 缩小时的过滤模式 GLES20.GL_TEXTURE_MIN_FILTER ； 放大时的过滤模式  GLES20.GL_TEXTURE_MAG_FILTER
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0); //将纹理加载到OpenGL中，及时回收bitmap
        bitmap.recycle();

        //为与target相关的纹理图像生成一组完整的mipmap
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);  //用于接触和纹理的绑定，等使用时再绑定

        return textureObjId[0];
    }
}
