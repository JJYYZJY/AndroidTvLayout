package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

import com.fb.jjyyzjy.buildlayoutdemo.utils.CastUtils;

/**
 * 自定义动态布局，辅助工具类
 * Created by ZJY on 2016/6/18.
 */
public class BuildLayoutUtils {

    /**
     * 根据服务器返回数据转换成对应宽高尺寸
     * @param sSize
     * @param mW
     * @param mH
     * @return
     */
    public static int[] getBuildLayoutSize(String sSize, int mW, int mH){
        int[] size = new int[2];
        if (sSize != null){
            String[] sizeLayout = sSize.split("\\*");
            if (sizeLayout.length >= 2){
                size[0] = CastUtils.getIntForString(sizeLayout[0],mW);
                size[1] = CastUtils.getIntForString(sizeLayout[1],mH);
            }else {
                size[0] = mW;
                size[1] = mH;
            }
        }else {
            size[0] = mW;
            size[1] = mH;
        }
        return size;
    }
}
