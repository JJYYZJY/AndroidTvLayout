package com.fb.jjyyzjy.buildlayoutdemo;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fb.jjyyzjy.buildlayoutdemo.view.buildview.BaseBuildLayoutAdapter;

/**
 * Created by zhangjy on 2018/4/24.
 */

public class BuildLayoutAdapter extends BaseBuildLayoutAdapter {

    private int[][] data;

    public void setData(int[][] data){
        this.data = data;
    }

    @Override
    public int[] getLayout(int position) {
        return data[position];
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        if (contentView == null){
            contentView = new ImageView(parent.getContext());
            contentView.setBackgroundColor(Color.BLUE);
        }
        return contentView;
    }


}
