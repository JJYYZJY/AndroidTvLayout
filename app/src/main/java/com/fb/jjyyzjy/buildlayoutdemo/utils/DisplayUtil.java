package com.fb.jjyyzjy.buildlayoutdemo.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by JJYYZJY on 2017/4/25.
 */
public class DisplayUtil {
    private static float scale;

    private static float scaledDensity;

    private static float widthPixels;

    private static float heightPixels;

    public static final int CHINESE = 0;

    public static final int NUMBER_OR_CHARACTER = 1;

    public static void init(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        scale = displayMetrics.density;
        scaledDensity = displayMetrics.scaledDensity;
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;

    }

    public static int dim2px(float dimValue){
        return (int) (dimValue*widthPixels/1920);
    }

    /**
     * dp转成px
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转成dp
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转成px
     * @param spValue
     * @param type
     * @return
     */
    public static float sp2px(float spValue, int type) {
        switch (type) {
            case CHINESE:
                return spValue * scaledDensity;
            case NUMBER_OR_CHARACTER:
                return spValue * scaledDensity * 10.0f / 18.0f;
            default:
                return spValue * scaledDensity;
        }
    }
}
