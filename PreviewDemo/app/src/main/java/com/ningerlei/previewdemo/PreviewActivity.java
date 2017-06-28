package com.ningerlei.previewdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Description :
 * CreateTime : 2017/6/28 14:43
 *
 * @author ningerlei@danale.com
 * @version <v1.0>
 * @Editor : Administrator
 * @ModifyTime : 2017/6/28 14:43
 * @ModifyDescription :
 */

public class PreviewActivity extends Activity{

    public static void startActivity(Context context, Class<? extends PreviewActivity> cls){
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }
}
