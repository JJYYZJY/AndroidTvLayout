package com.fb.jjyyzjy.buildlayoutdemo.view.bring;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JJYYZJY on 2017/9/6.
 */
public class BringToFrontHelper {

    private int mFocusChildIndex;

    public void bringChildToFront(ViewGroup viewGroup, View childView){
        mFocusChildIndex = viewGroup.indexOfChild(childView);
        if(mFocusChildIndex != -1){
            viewGroup.postInvalidate();
        }
    }

    public int getChildDrawingOrder(int childCount, int i){
        if(mFocusChildIndex != -1){
            if(i == childCount-1){
                if(mFocusChildIndex > childCount-1){
                    mFocusChildIndex = childCount-1;
                }
                return mFocusChildIndex;
            }
            if(i == mFocusChildIndex){

                return childCount -1;
            }
        }
        if(childCount <= i){
            i = childCount-1;
        }
        return i;
    }
}
