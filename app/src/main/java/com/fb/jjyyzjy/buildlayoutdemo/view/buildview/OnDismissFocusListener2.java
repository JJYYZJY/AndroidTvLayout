package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

import android.view.View;

/**
 * 失去焦点2_带返回_true为截断事件
 * Created by ZJY on 2016/4/26.
 */
public interface OnDismissFocusListener2 {
    public boolean onDismissForTop(View v);
    public boolean onDismissForBot(View v);
    public boolean onDismissForLeft(View v);
    public boolean onDismissForRight(View v);
}
