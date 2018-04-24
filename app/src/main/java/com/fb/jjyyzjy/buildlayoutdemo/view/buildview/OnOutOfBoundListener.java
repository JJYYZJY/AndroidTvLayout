package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

import android.view.View;

/**
 * 宽度超多视图窗口的view焦点超过视图监听
 * Created by ZJY on 2016/5/12.
 */
public interface OnOutOfBoundListener {
    void onOutForRight(View v);
    void onOutForLeft(View v);
}
