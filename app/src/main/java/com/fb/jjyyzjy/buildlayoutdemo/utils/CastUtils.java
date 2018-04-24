package com.fb.jjyyzjy.buildlayoutdemo.utils;

/**
 * 安全的类型转换
 * Created by ZJY on 2016/5/19.
 */
public class CastUtils {

    public static int getIntForString(String str, int defaultNum){
        try{
            defaultNum = Integer.parseInt(str);
        }catch (Exception e){
            DebugLog.e("CastUtils_getIntForString Exception=="+e);
        }
        return defaultNum;
    }

    public static long getLongForString(String str, long defaultNum){
        try{
            defaultNum = Long.parseLong(str);
        }catch (Exception e){
            DebugLog.e("CastUtils_getLongForString Exception=="+e);
        }
        return defaultNum;
    }

}
